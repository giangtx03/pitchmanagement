package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.constants.AuthConstant;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.dtos.*;
import com.pitchmanagement.exceptions.InvalidDataException;
import com.pitchmanagement.models.requests.image.CreateImageRequest;
import com.pitchmanagement.models.requests.pitch.CreatePitchRequest;
import com.pitchmanagement.models.requests.pitch.PitchTimeRequest;
import com.pitchmanagement.models.requests.pitch.SubPitchRequest;
import com.pitchmanagement.models.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;
import com.pitchmanagement.models.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.models.responses.pitch.SubPitchResponse;
import com.pitchmanagement.models.responses.UserResponse;
import com.pitchmanagement.services.ImageService;
import com.pitchmanagement.services.PitchService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PitchServiceImpl implements PitchService {
    private final PitchDao pitchDao;
    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;
    private final ImageDao imageDao;
    private final PitchTypeDao pitchTypeDao;
    private final UserDao userDao;
    private final TimeSlotDao timeSlotDao;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(PitchServiceImpl.class);
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public PitchResponse createPitch(CreatePitchRequest request) throws Exception {

        if (request.getImages() != null && request.getImages().size() > 5) {
            throw new IllegalArgumentException("Chỉ cho phép mỗi sân tối đa 5 ảnh");
        }

        UserDto managerDto = userDao.getUserById(request.getManagerId());

        if(managerDto == null || !managerDto.getRole().equals(AuthConstant.ROLE_MANAGER)){
            logger.info("Không tìm thấy người chủ sân với id : {}", request.getManagerId());
            throw new NotFoundException("Không tồn tại quản lý sân bóng!!!");
        }

        PitchDto pitchDto = PitchDto.builder()
                .name(request.getName())
                .location(request.getLocation())
                .managerId(request.getManagerId())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .isActive(true)
                .build();
        pitchDao.insertPitch(pitchDto);
        List<SubPitchResponse> subPitchResponses = new ArrayList<>();
        for(SubPitchRequest subPitchRequest : request.getSubPitches()){
            PitchTypeDto pitchTypeDto = pitchTypeDao.getPitchTypeById(subPitchRequest.getPitchTypeId());

            if(pitchTypeDto == null){
                logger.info("Không tìm thấy sân bóng với id : {}", subPitchRequest.getPitchTypeId());
                throw new NotFoundException("Không thấy loại sân bóng!!!");
            }

            SubPitchDto subPitchDto = SubPitchDto.builder()
                    .name(subPitchRequest.getName())
                    .isActive(true)
                    .pitchId(pitchDto.getId())
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .pitchTypeId(subPitchRequest.getPitchTypeId())
                    .build();
            subPitchDao.insertSubPitch(subPitchDto);
            List<PitchTimeResponse> pitchTimeResponses = new ArrayList<>();
            for(PitchTimeRequest pitchTimeRequest : subPitchRequest.getPitchTimes()){
                TimeSlotDto timeSlotDto = timeSlotDao.getTimeSlotById(pitchTimeRequest.getTimeSlotId());
                if(timeSlotDto == null) {
                    logger.info("Không tìm thấy khung giờ với id : {}", pitchTimeRequest.getTimeSlotId());
                    throw new NotFoundException("Không thấy khung giờ!!!");
                }
                PitchTimeDto pitchTimeDto = PitchTimeDto.builder()
                        .price(pitchTimeRequest.getPrice())
                        .subPitchId(subPitchDto.getId())
                        .timeSlotId(timeSlotDto.getId())
                        .isActive(true)
                        .build();
                pitchTimeDao.insertPitchTime(pitchTimeDto);
                PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                        .startTime(timeSlotDto.getStartTime())
                        .endTime(timeSlotDto.getEndTime())
                        .price(pitchTimeDto.getPrice())
                        .build();
                pitchTimeResponses.add(pitchTimeResponse);
            }
            SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                    .id(subPitchDto.getId())
                    .name(subPitchDto.getName())
                    .createAt(subPitchDto.getCreateAt())
                    .updateAt(subPitchDto.getUpdateAt())
                    .pitchTimes(pitchTimeResponses)
                    .pitchType(pitchTypeDto.getName())
                    .build();
            subPitchResponses.add(subPitchResponse);
        }

        List<String> imageResponse = new ArrayList<>();
        for(MultipartFile image : request.getImages()){
            String imageName = imageService.upload(image);
            ImageDto imageDto = ImageDto.builder()
                    .pitchId(pitchDto.getId())
                    .name(imageName)
                    .build();
            imageDao.insertImage(imageDto);
            imageResponse.add(imageName);
        }

        UserResponse managerResponse = UserResponse.fromUserDto(managerDto);

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitchDto.getId())
                .name(pitchDto.getName())
                .manager(managerResponse)
                .location(pitchDto.getLocation())
                .subPitches(subPitchResponses)
                .images(imageResponse)
                .createAt(pitchDto.getCreateAt())
                .updateAt(pitchDto.getUpdateAt())
                .build();
        return pitchResponse;
    }

    @Override
    public PageResponse getAll(String keyword, int startPrice, int endPrice,
                               int starRange, long[] pitchTypes,
                               int pageNumber, int limit, String orderBy, String orderSort) {
        PageHelper.startPage(pageNumber, limit);
        PageHelper.orderBy(orderBy + " " + orderSort);
        List<PitchDto> pitchDtoList = pitchDao.getAll(keyword, startPrice, endPrice,starRange, pitchTypes);
        PageInfo<PitchDto> pageInfo = new PageInfo<>(pitchDtoList);

        List<PitchResponse> pitchResponseList = pitchDtoList.stream()
                .map(pitchDto -> {
                        List<String> imagesResponse = imageDao.getAllByPitchId(pitchDto.getId())
                                .stream().map(ImageDto::getName)
                                .toList();
                        return PitchResponse.builder()
                            .id(pitchDto.getId())
                            .name(pitchDto.getName())
                            .location(pitchDto.getLocation())
                            .manager(UserResponse.fromUserDto(pitchDto.getManagerDto()))
                            .createAt(pitchDto.getCreateAt())
                            .updateAt(pitchDto.getUpdateAt())
                            .isActive(pitchDto.isActive())
                            .images(imagesResponse)
                            .avgStar(pitchDto.getAvgStar())
                        .build();

                })
                .toList();

        PageResponse pageResponse = PageResponse.builder()
                .items(pitchResponseList)
                .totalItems(pageInfo.getTotal())
                .totalPages(pageInfo.getPages())
                .build();
        return pageResponse;
    }

    @Override
    public PitchResponse getPitchById(Long id) throws Exception {
        PitchDto pitchDto = pitchDao.getPitchById(id);

        if(pitchDto == null){
            throw new NotFoundException("Không tìm thấy sân bóng!");
        }

        UserDto managerDto = userDao.getUserById(pitchDto.getManagerId());
        UserResponse managerResponse = UserResponse.fromUserDto(managerDto);
        List<SubPitchDto> subPitchDtos = subPitchDao.getAllByPitchId(pitchDto.getId());
        List<SubPitchResponse> subPitchResponses = subPitchDtos.stream()
                .map(
                subPitchDto -> {
                    List<PitchTimeResponse> pitchTimeResponses = pitchTimeDao.getPitchTimeBySubPitchId(subPitchDto.getId())
                            .stream()
                            .map(pitchTimeDto ->
                                PitchTimeResponse.builder()
                                        .startTime(pitchTimeDto.getStartTime())
                                        .endTime(pitchTimeDto.getEndTime())
                                        .price(pitchTimeDto.getPrice())
                                        .isActive(pitchTimeDto.isActive())
                                        .build()
                            ).toList();
                    return SubPitchResponse.builder()
                            .id(subPitchDto.getId())
                            .name(subPitchDto.getName())
                            .pitchType(subPitchDto.getPitchType())
                            .pitchTimes(pitchTimeResponses)
                            .createAt(subPitchDto.getCreateAt())
                            .updateAt(subPitchDto.getUpdateAt())
                            .isActive(subPitchDto.isActive())
                            .build();
                }
        ).toList();

        List<String> imageResponses = imageDao.getAllByPitchId(pitchDto.getId())
                .stream().map(ImageDto::getName).toList();

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitchDto.getId())
                .name(pitchDto.getName())
                .location(pitchDto.getLocation())
                .subPitches(subPitchResponses)
                .manager(managerResponse)
                .images(imageResponses)
                .createAt(pitchDto.getCreateAt())
                .updateAt(pitchDto.getUpdateAt())
                .isActive(pitchDto.isActive())
                .avgStar(pitchDto.getAvgStar())
                .build();
        return pitchResponse;
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public PitchResponse updatePitch(UpdatePitchRequest updatePitchRequest) throws Exception {
        PitchDto pitchDto = pitchDao.getPitchById(updatePitchRequest.getId());

        if(pitchDto == null) {
            logger.warn("Không tìm thấy sân bóng với id : {}", updatePitchRequest.getId());
            throw new NotFoundException("Không tìm thấy sân bóng!");
        }

        if(!Objects.equals(updatePitchRequest.getManagerId(), pitchDto.getManagerId())){
            logger.warn("Lỗi thông tin chủ sở hữu");
            throw new AccountException("Bạn không phải là chủ sân bóng này!");
        }

        pitchDto.setName(updatePitchRequest.getName());
        pitchDto.setLocation(updatePitchRequest.getLocation());
        pitchDto.setActive(updatePitchRequest.isActive());
        pitchDto.setUpdateAt(LocalDateTime.now());

        pitchDao.updatePitch(pitchDto);
        logger.info("Cập nhật sân bóng thành công : {}", updatePitchRequest.getName());
        return getPitchById(updatePitchRequest.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addImages(CreateImageRequest imageRequest) throws Exception {
        PitchDto pitchDto = pitchDao.getPitchById(imageRequest.getPitchId());

        if(pitchDto == null) {
            logger.warn("Không tìm thấy sân bóng với id : {}", imageRequest.getPitchId());
            throw new NotFoundException("Không tìm thấy sân bóng!");
        }

        List<ImageDto> imageDtos = imageDao.getAllByPitchId(imageRequest.getPitchId());

        if(imageDtos.size() + imageRequest.getImages().size() > 5){
            throw new InvalidDataException("Tổng số ảnh cho sân bóng này vượt quá 5!");
        }

        for(MultipartFile image : imageRequest.getImages()){
            String imageName = imageService.upload(image);
            ImageDto imageDto = ImageDto.builder()
                    .pitchId(pitchDto.getId())
                    .name(imageName)
                    .build();
            imageDao.insertImage(imageDto);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteImage(String name) {
        try {
            imageService.delete(name);
            imageDao.deleteImage(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
