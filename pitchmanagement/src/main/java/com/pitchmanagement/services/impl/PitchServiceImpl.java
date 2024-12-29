package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.constants.AuthConstant;
import com.pitchmanagement.enums.BookingStatus;
import com.pitchmanagement.enums.PitchTimeStatus;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.models.*;
import com.pitchmanagement.exceptions.InvalidDataException;
import com.pitchmanagement.dtos.requests.image.CreateImageRequest;
import com.pitchmanagement.dtos.requests.pitch.CreatePitchRequest;
import com.pitchmanagement.dtos.requests.pitch.PitchTimeRequest;
import com.pitchmanagement.dtos.requests.pitch.SubPitchRequest;
import com.pitchmanagement.dtos.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.dtos.responses.PageResponse;
import com.pitchmanagement.dtos.responses.pitch.PitchResponse;
import com.pitchmanagement.dtos.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.dtos.responses.pitch.SubPitchResponse;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.services.ImageService;
import com.pitchmanagement.services.PitchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PitchServiceImpl implements PitchService {
    private final PitchDao pitchDao;
    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;
    private final ImageDao imageDao;
    private final PitchTypeDao pitchTypeDao;
    private final UserDao userDao;
    private final TimeSlotDao timeSlotDao;
    private final ImageService imageService;
    private final BookingDao bookingDao;
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public PitchResponse createPitch(CreatePitchRequest request) throws Exception {

        if (request.getImages() != null && request.getImages().size() > 5) {
            throw new IllegalArgumentException("Chỉ cho phép mỗi sân tối đa 5 ảnh");
        }

        User managerDto = userDao.getUserById(request.getManagerId());

        if(managerDto == null || !managerDto.getRole().equals(AuthConstant.ROLE_MANAGER)){
            log.info("Không tìm thấy người chủ sân với id : {}", request.getManagerId());
            throw new NotFoundException("Không tồn tại quản lý sân bóng!!!");
        }

        Pitch pitch = Pitch.builder()
                .name(request.getName())
                .location(request.getLocation())
                .managerId(request.getManagerId())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .isActive(true)
                .build();
        pitchDao.insertPitch(pitch);
        List<SubPitchResponse> subPitchResponses = new ArrayList<>();
        for(SubPitchRequest subPitchRequest : request.getSubPitches()){
            PitchType pitchType = pitchTypeDao.getPitchTypeById(subPitchRequest.getPitchTypeId());

            if(pitchType == null){
                log.info("Không tìm thấy sân bóng với id : {}", subPitchRequest.getPitchTypeId());
                throw new NotFoundException("Không thấy loại sân bóng!!!");
            }

            SubPitch subPitchDto = SubPitch.builder()
                    .name(subPitchRequest.getName())
                    .isActive(true)
                    .pitchId(pitch.getId())
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .pitchTypeId(subPitchRequest.getPitchTypeId())
                    .build();
            subPitchDao.insertSubPitch(subPitchDto);
            List<PitchTimeResponse> pitchTimeResponses = new ArrayList<>();
            for(PitchTimeRequest pitchTimeRequest : subPitchRequest.getPitchTimes()){
                TimeSlot timeSlotDto = timeSlotDao.getTimeSlotById(pitchTimeRequest.getTimeSlotId());
                if(timeSlotDto == null) {
                    log.info("Không tìm thấy khung giờ với id : {}", pitchTimeRequest.getTimeSlotId());
                    throw new NotFoundException("Không thấy khung giờ!!!");
                }
                PitchTime pitchTime = PitchTime.builder()
                        .price(pitchTimeRequest.getPrice())
                        .subPitchId(subPitchDto.getId())
                        .timeSlotId(timeSlotDto.getId())
                        .isActive(true)
                        .build();
                pitchTimeDao.insertPitchTime(pitchTime);
                PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                        .startTime(timeSlotDto.getStartTime())
                        .endTime(timeSlotDto.getEndTime())
                        .price(pitchTime.getPrice())
                        .build();
                pitchTimeResponses.add(pitchTimeResponse);
            }
            SubPitchResponse subPitchResponse = SubPitchResponse.builder()
                    .id(subPitchDto.getId())
                    .name(subPitchDto.getName())
                    .createAt(subPitchDto.getCreateAt())
                    .updateAt(subPitchDto.getUpdateAt())
                    .pitchTimes(pitchTimeResponses)
                    .pitchType(pitchType.getName())
                    .build();
            subPitchResponses.add(subPitchResponse);
        }

        List<String> imageResponse = new ArrayList<>();
        for(MultipartFile image : request.getImages()){
            String imageName = imageService.upload(image);
            Image imageDto = Image.builder()
                    .pitchId(pitch.getId())
                    .name(imageName)
                    .build();
            imageDao.insertImage(imageDto);
            imageResponse.add(imageName);
        }

        UserResponse managerResponse = UserResponse.fromUserDto(managerDto);

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitch.getId())
                .name(pitch.getName())
                .manager(managerResponse)
                .location(pitch.getLocation())
                .subPitches(subPitchResponses)
                .images(imageResponse)
                .createAt(pitch.getCreateAt())
                .updateAt(pitch.getUpdateAt())
                .build();
        return pitchResponse;
    }

    @Override
    public PageResponse getAll(String keyword, int startPrice, int endPrice,
                               Long managerId,
                               int starRange, long[] pitchTypes,
                               boolean requestQuery,
                               int pageNumber, int limit, String orderBy, String orderSort) {
        PageHelper.startPage(pageNumber, limit);
        PageHelper.orderBy(orderBy + " " + orderSort);
        List<Pitch> pitchList = pitchDao.getAll(keyword, startPrice, endPrice,managerId,starRange, pitchTypes, requestQuery);
        PageInfo<Pitch> pageInfo = new PageInfo<>(pitchList);

        List<PitchResponse> pitchResponseList = pitchList.stream()
                .map(pitchDto -> {
                        List<String> imagesResponse = imageDao.getAllByPitchId(pitchDto.getId())
                                .stream().map(Image::getName)
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
    public PitchResponse getPitchById(Long id, boolean requestQuery) throws Exception {
        Pitch pitch = pitchDao.getPitchById(id, requestQuery);

        if(pitch == null){
            throw new NotFoundException("Không tìm thấy sân bóng!");
        }

        User managerDto = userDao.getUserById(pitch.getManagerId());
        UserResponse managerResponse = UserResponse.fromUserDto(managerDto);
        List<SubPitch> subPitchDtos = subPitchDao.getAllByPitchId(pitch.getId(), requestQuery);
        List<SubPitchResponse> subPitchResponses = subPitchDtos.stream()
                .map(
                subPitchDto -> {
                    List<PitchTimeResponse> pitchTimeResponses = pitchTimeDao.getPitchTimeBySubPitchId(subPitchDto.getId(), requestQuery)
                            .stream()
                            .map(pitchTime -> {
                                PitchTimeResponse pitchTimeResponse = PitchTimeResponse.builder()
                                        .startTime(pitchTime.getStartTime())
                                        .endTime(pitchTime.getEndTime())
                                        .price(pitchTime.getPrice())
                                        .timeSlotId(pitchTime.getTimeSlotId())
                                        .isActive(pitchTime.isActive())
                                        .build();
                                List<String> schedules = new ArrayList<>();
                                for(long i = 0; i < 7; i++){
                                    if(bookingDao.isExistingBooking(subPitchDto.getId(), pitchTime.getTimeSlotId(),
                                            LocalDate.now().plusDays(i), BookingStatus.CANCELLED.toString())){
                                        schedules.add(PitchTimeStatus.ORDERED.toString());
                                    }
                                    else schedules.add(PitchTimeStatus.OPENED.toString());
                                }
                                pitchTimeResponse.setSchedules(schedules);
                                return pitchTimeResponse;
                            }
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

        List<String> imageResponses = imageDao.getAllByPitchId(pitch.getId())
                .stream().map(Image::getName).toList();

        PitchResponse pitchResponse = PitchResponse.builder()
                .id(pitch.getId())
                .name(pitch.getName())
                .location(pitch.getLocation())
                .subPitches(subPitchResponses)
                .manager(managerResponse)
                .images(imageResponses)
                .createAt(pitch.getCreateAt())
                .updateAt(pitch.getUpdateAt())
                .isActive(pitch.isActive())
                .avgStar(pitch.getAvgStar())
                .build();
        return pitchResponse;
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public PitchResponse updatePitch(UpdatePitchRequest updatePitchRequest) throws Exception {
        Pitch pitch = pitchDao.getPitchById(updatePitchRequest.getId(), false);

        if(pitch == null) {
            log.warn("Không tìm thấy sân bóng với id : {}", updatePitchRequest.getId());
            throw new NotFoundException("Không tìm thấy sân bóng!");
        }

        if(!Objects.equals(updatePitchRequest.getManagerId(), pitch.getManagerId())){
            log.warn("Lỗi thông tin chủ sở hữu");
            throw new AccountException("Bạn không phải là chủ sân bóng này!");
        }

        pitch.setName(updatePitchRequest.getName());
        pitch.setLocation(updatePitchRequest.getLocation());
        pitch.setActive(updatePitchRequest.isActive());
        pitch.setUpdateAt(LocalDateTime.now());

        pitchDao.updatePitch(pitch);
        log.info("Cập nhật sân bóng thành công : {}", updatePitchRequest.getName());
        return getPitchById(updatePitchRequest.getId(), false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addImages(CreateImageRequest imageRequest) throws Exception {
        Pitch pitch = pitchDao.getPitchById(imageRequest.getPitchId(), false);

        if(pitch == null) {
            log.warn("Không tìm thấy sân bóng với id : {}", imageRequest.getPitchId());
            throw new NotFoundException("Không tìm thấy sân bóng!");
        }

        List<Image> imageDtos = imageDao.getAllByPitchId(imageRequest.getPitchId());

        if(imageDtos.size() + imageRequest.getImages().size() > 5){
            throw new InvalidDataException("Tổng số ảnh cho sân bóng này vượt quá 5!");
        }

        for(MultipartFile image : imageRequest.getImages()){
            String imageName = imageService.upload(image);
            Image imageDto = Image.builder()
                    .pitchId(pitch.getId())
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
