package com.pitchmanagement.services.impl;

import com.pitchmanagement.constants.BookingStatus;
import com.pitchmanagement.constants.PitchTimeStatus;
import com.pitchmanagement.daos.*;
import com.pitchmanagement.dtos.*;
import com.pitchmanagement.models.requests.pitch_time.CreatePitchTimeRequest;
import com.pitchmanagement.models.requests.pitch_time.UpdatePitchTimeRequest;
import com.pitchmanagement.models.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.models.responses.pitch.SubPitchResponse;
import com.pitchmanagement.services.PitchTimeService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PitchTimeServiceImpl implements PitchTimeService {

    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;
    private final TimeSlotDao timeSlotDao;
    private final Logger logger = LoggerFactory.getLogger(PitchTimeServiceImpl.class);

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void createPitchTime(CreatePitchTimeRequest createPitchTimeRequest) throws Exception {
        SubPitchDto subPitchDto = subPitchDao.getSubPitchById(createPitchTimeRequest.getSubPitchId());
        TimeSlotDto timeSlotDto = timeSlotDao.getTimeSlotById(createPitchTimeRequest.getTimeSlotId());

        if(subPitchDto == null){
            logger.warn("Không tìm thấy sân con với id sân : {}", createPitchTimeRequest.getSubPitchId());
            throw new NotFoundException("Không tìm thấy sân con");
        }
        if(timeSlotDto == null){
            logger.warn("Không tìm thấy khun giờ với id : {}", createPitchTimeRequest.getTimeSlotId());
            throw new NotFoundException("Không tìm thấy khung giờ");
        }

        if(pitchTimeDao.isExisting(createPitchTimeRequest.getSubPitchId(), createPitchTimeRequest.getTimeSlotId())){
            logger.warn("Khung thời gian : {} - {} đã tồn tại trong sân : {}",timeSlotDto.getStartTime(), timeSlotDto.getEndTime(), subPitchDto.getName());
            throw new RuntimeException("Khung thời gian đã tồn tại cho sân này");
        }

        PitchTimeDto pitchTimeDto = PitchTimeDto.builder()
                .price(createPitchTimeRequest.getPrice())
                .subPitchId(createPitchTimeRequest.getSubPitchId())
                .timeSlotId(createPitchTimeRequest.getTimeSlotId())
                .isActive(true)
                .build();
        pitchTimeDao.insertPitchTime(pitchTimeDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void updatePitchTime(UpdatePitchTimeRequest updatePitchTimeRequest) throws Exception {
        TimeSlotDto timeSlotDto = timeSlotDao.getTimeSlotById(updatePitchTimeRequest.getTimeSlotId());

        if(timeSlotDto == null){
            logger.warn("Không tìm thấy khun giờ với id : {}", updatePitchTimeRequest.getTimeSlotId());
            throw new NotFoundException("Không tìm thấy khung giờ");
        }

        if(!pitchTimeDao.isExisting(updatePitchTimeRequest.getSubPitchId(), updatePitchTimeRequest.getTimeSlotId())){
            logger.warn("Khung thời gian : {} - {} chưa tồn tại trong sân với id : {}",timeSlotDto.getStartTime(), timeSlotDto.getEndTime(), updatePitchTimeRequest.getSubPitchId());
            throw new RuntimeException("Khung thời gian đã tồn tại cho sân này");
        }

        PitchTimeDto pitchTimeDto = PitchTimeDto.builder()
                .price(updatePitchTimeRequest.getPrice())
                .isActive(updatePitchTimeRequest.isActive())
                .timeSlotId(updatePitchTimeRequest.getTimeSlotId())
                .subPitchId(updatePitchTimeRequest.getSubPitchId())
                .build();
        pitchTimeDao.updatePitchTime(pitchTimeDto);
    }

}
