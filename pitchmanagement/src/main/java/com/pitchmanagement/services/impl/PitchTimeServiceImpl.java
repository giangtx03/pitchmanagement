package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.*;
import com.pitchmanagement.models.*;
import com.pitchmanagement.dtos.requests.pitch_time.CreatePitchTimeRequest;
import com.pitchmanagement.dtos.requests.pitch_time.UpdatePitchTimeRequest;
import com.pitchmanagement.services.PitchTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PitchTimeServiceImpl implements PitchTimeService {

    private final PitchTimeDao pitchTimeDao;
    private final SubPitchDao subPitchDao;
    private final TimeSlotDao timeSlotDao;

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void createPitchTime(CreatePitchTimeRequest createPitchTimeRequest) throws Exception {
        SubPitch subPitchDto = subPitchDao.getSubPitchById(createPitchTimeRequest.getSubPitchId());
        TimeSlot timeSlotDto = timeSlotDao.getTimeSlotById(createPitchTimeRequest.getTimeSlotId());

        if(subPitchDto == null){
            log.warn("Không tìm thấy sân con với id sân : {}", createPitchTimeRequest.getSubPitchId());
            throw new NotFoundException("Không tìm thấy sân con");
        }
        if(timeSlotDto == null){
            log.warn("Không tìm thấy khun giờ với id : {}", createPitchTimeRequest.getTimeSlotId());
            throw new NotFoundException("Không tìm thấy khung giờ");
        }

        if(pitchTimeDao.isExisting(createPitchTimeRequest.getSubPitchId(), createPitchTimeRequest.getTimeSlotId())){
            log.warn("Khung thời gian : {} - {} đã tồn tại trong sân : {}",timeSlotDto.getStartTime(), timeSlotDto.getEndTime(), subPitchDto.getName());
            throw new RuntimeException("Khung thời gian đã tồn tại cho sân này");
        }

        PitchTime pitchTime = PitchTime.builder()
                .price(createPitchTimeRequest.getPrice())
                .subPitchId(createPitchTimeRequest.getSubPitchId())
                .timeSlotId(createPitchTimeRequest.getTimeSlotId())
                .isActive(true)
                .build();
        pitchTimeDao.insertPitchTime(pitchTime);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void updatePitchTime(UpdatePitchTimeRequest updatePitchTimeRequest) throws Exception {
        TimeSlot timeSlotDto = timeSlotDao.getTimeSlotById(updatePitchTimeRequest.getTimeSlotId());

        if(timeSlotDto == null){
            log.warn("Không tìm thấy khun giờ với id : {}", updatePitchTimeRequest.getTimeSlotId());
            throw new NotFoundException("Không tìm thấy khung giờ");
        }

        if(!pitchTimeDao.isExisting(updatePitchTimeRequest.getSubPitchId(), updatePitchTimeRequest.getTimeSlotId())){
            log.warn("Khung thời gian : {} - {} chưa tồn tại trong sân với id : {}",timeSlotDto.getStartTime(), timeSlotDto.getEndTime(), updatePitchTimeRequest.getSubPitchId());
            throw new RuntimeException("Khung thời gian đã tồn tại cho sân này");
        }

        PitchTime pitchTime = PitchTime.builder()
                .price(updatePitchTimeRequest.getPrice())
                .isActive(updatePitchTimeRequest.isActive())
                .timeSlotId(updatePitchTimeRequest.getTimeSlotId())
                .subPitchId(updatePitchTimeRequest.getSubPitchId())
                .build();
        pitchTimeDao.updatePitchTime(pitchTime);
    }

}
