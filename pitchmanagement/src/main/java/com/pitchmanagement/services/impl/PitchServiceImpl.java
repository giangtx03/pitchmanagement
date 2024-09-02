package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.AuthConstant;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.dtos.*;
import com.pitchmanagement.models.requests.createPitch.CreatePitchRequest;
import com.pitchmanagement.models.requests.createPitch.PitchTimeRequest;
import com.pitchmanagement.models.requests.createPitch.SubPitchRequest;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
