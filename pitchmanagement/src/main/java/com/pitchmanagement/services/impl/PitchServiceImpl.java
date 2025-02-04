package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.enums.BookingStatus;
import com.pitchmanagement.enums.PitchTimeStatus;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.mapper.PitchMapper;
import com.pitchmanagement.mapper.PitchTimeMapper;
import com.pitchmanagement.mapper.SubPitchMapper;
import com.pitchmanagement.mapper.UserMapper;
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
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PitchServiceImpl implements PitchService {
    private final PitchDao pitchDao;
    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;
    private final ImageDao imageDao;
    private final UserDao userDao;
    private final ImageService imageService;
    private final BookingDao bookingDao;
    private final PitchMapper pitchMapper;
    private final SubPitchMapper subPitchMapper;
    private final PitchTimeMapper pitchTimeMapper;
    private final UserMapper userMapper;
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public PitchResponse createPitch(CreatePitchRequest request) throws Exception {
        User manager = Optional.ofNullable(userDao.getUserById(request.getManagerId()))
                .orElseThrow(() -> new NotFoundException("Không tồn tại quản lý sân bóng!!!"));

        Pitch pitch = pitchMapper.toPitch(request);
        pitchDao.insertPitch(pitch);

//        List<SubPitchResponse> subPitchResponses = new ArrayList<>();
//        for(SubPitchRequest subPitchRequest : request.getSubPitches()){
//            PitchType pitchType = Optional.ofNullable(pitchTypeDao.getPitchTypeById(subPitchRequest.getPitchTypeId()))
//                    .orElseThrow(() -> new NotFoundException("Không thấy loại sân bóng!!!"));
//
//            SubPitch subPitch = subPitchMapper.toSubPitch(subPitchRequest);
//            subPitchDao.insertSubPitch(subPitch);
//
//            List<PitchTimeResponse> pitchTimeResponses = new ArrayList<>();
//            for(PitchTimeRequest pitchTimeRequest : subPitchRequest.getPitchTimes()){
//                TimeSlot timeSlot = Optional.ofNullable(timeSlotDao.getTimeSlotById(pitchTimeRequest.getTimeSlotId()))
//                        .orElseThrow(() -> new NotFoundException("Không thấy khung giờ!!!"));
//
//                PitchTime pitchTime = pitchTimeMapper.toPitchTime(timeSlot, pitchTimeRequest.getPrice());
//                PitchTimeResponse pitchTimeResponse = pitchTimeMapper.toPitchTimeResponse(pitchTime);
//                pitchTimeResponses.add(pitchTimeResponse);
//                pitchTimeDao.insertPitchTime(pitchTime);
//            }
//            SubPitchResponse subPitchResponse = subPitchMapper.toSubPitchResponse(subPitch, pitchType.getName(), pitchTimeResponses);
//            subPitchResponses.add(subPitchResponse);
//        }

        UserResponse managerResponse = userMapper.toUserResponse(manager);
        PitchResponse pitchResponse = pitchMapper.toPitchResponse(pitch, managerResponse, null, null);
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
                .map(pitch -> {
                        List<String> imagesResponse = imageDao.getAllByPitchId(pitch.getId())
                                .stream().map(Image::getName)
                                .toList();
                        return pitchMapper.toPitchResponse(pitch,
                                userMapper.toUserResponse(pitch.getManagerDto()),
                                null, imagesResponse);
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
        Pitch pitch = Optional.ofNullable(pitchDao.getPitchById(id, requestQuery))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sân bóng!"));

        User manager = Optional.ofNullable(userDao.getUserById(pitch.getManagerId()))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy quản lý sân bóng!"));
        UserResponse managerResponse = userMapper.toUserResponse(manager);
        List<SubPitch> subPitchList = subPitchDao.getAllByPitchId(pitch.getId(), requestQuery);
        List<SubPitchResponse> subPitchResponses = subPitchList.stream().map(
                subPitch -> {
                    List<PitchTimeResponse> pitchTimeResponses =
                            pitchTimeDao.getPitchTimeBySubPitchId(subPitch.getId(), requestQuery)
                            .stream()
                            .map(pitchTime -> {
                                PitchTimeResponse pitchTimeResponse = pitchTimeMapper.toPitchTimeResponse( pitchTime );
                                List<String> schedules = IntStream.range(0, 7)
                                        .mapToObj(i -> bookingDao.isExistingBooking(subPitch.getId(), pitchTime.getTimeSlotId(),
                                                LocalDate.now().plusDays(i), BookingStatus.CANCELLED.toString()) ?
                                                PitchTimeStatus.ORDERED.toString() : PitchTimeStatus.OPENED.toString())
                                        .toList();
                                pitchTimeResponse.setSchedules(schedules);
                                return pitchTimeResponse;
                            }
                            ).toList();
                    return subPitchMapper.toSubPitchResponse(subPitch, subPitch.getPitchType(), pitchTimeResponses);
                }
        ).toList();

        List<String> imageResponses = imageDao.getAllByPitchId(pitch.getId())
                .stream().map(Image::getName).toList();

        PitchResponse pitchResponse = pitchMapper.toPitchResponse(
                pitch, managerResponse, subPitchResponses, imageResponses);
        return pitchResponse;
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public PitchResponse updatePitch(UpdatePitchRequest updatePitchRequest) throws Exception {
        Pitch pitch = Optional.ofNullable(pitchDao.getPitchById(updatePitchRequest.getId(), false))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sân bóng!"));

        pitchMapper.updatePitch(updatePitchRequest, pitch);
        pitchDao.updatePitch(pitch);
        log.info("Cập nhật sân bóng thành công : {}", updatePitchRequest.getName());
        return getPitchById(updatePitchRequest.getId(), false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addImages(CreateImageRequest imageRequest) throws Exception {
        Pitch pitch = Optional.ofNullable(pitchDao.getPitchById(imageRequest.getPitchId(), false))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sân bóng!"));

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
