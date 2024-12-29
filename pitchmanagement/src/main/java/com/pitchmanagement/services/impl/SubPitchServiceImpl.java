package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.PitchDao;
import com.pitchmanagement.daos.PitchTypeDao;
import com.pitchmanagement.daos.SubPitchDao;
import com.pitchmanagement.models.Pitch;
import com.pitchmanagement.models.PitchType;
import com.pitchmanagement.models.SubPitch;
import com.pitchmanagement.exceptions.InvalidDataException;
import com.pitchmanagement.dtos.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.dtos.requests.sub_pitch.UpdateSubPitchRequest;
import com.pitchmanagement.services.SubPitchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubPitchServiceImpl implements SubPitchService {

    private final SubPitchDao subPitchDao;
    private final PitchTypeDao pitchTypeDao;
    private final PitchDao pitchDao;
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void createSubPitch(CreateSubPitchRequest createSubPitchRequest) throws Exception {
        Pitch pitch = pitchDao.getPitchById(createSubPitchRequest.getPitchId(), false);
        PitchType pitchType = pitchTypeDao.getPitchTypeById(createSubPitchRequest.getPitchTypeId());

        if(pitch == null){
            log.warn("Không tìm thấy sân bóng với id : {}", createSubPitchRequest.getPitchId());
            throw new NotFoundException("Không tìm thấy sân bóng");
        }
        if(pitchType == null){
            log.warn("Không tìm thấy kiểu sân bóng với id : {}", createSubPitchRequest.getPitchTypeId());
            throw new NotFoundException("Không tìm thấy kiểu sân bóng");
        }

        if(subPitchDao.isExisting(createSubPitchRequest.getName(), createSubPitchRequest.getPitchId())){
            log.warn("Tên sân {} đã tồn tại trong sân bóng : {}",createSubPitchRequest.getName(), pitch.getName());
            throw new InvalidDataException("Sân nhỏ với tên này đã tồn tại trong sân bóng!");
        }

        SubPitch subPitchDto = SubPitch.builder()
                .name(createSubPitchRequest.getName())
                .pitchTypeId(createSubPitchRequest.getPitchTypeId())
                .pitchId(createSubPitchRequest.getPitchId())
                .isActive(true)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        subPitchDao.insertSubPitch(subPitchDto);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void updateSubPitch(UpdateSubPitchRequest updateSubPitchRequest) throws Exception {
        SubPitch subPitchDto = subPitchDao.getSubPitchById(updateSubPitchRequest.getId());
        if(subPitchDto == null){
            throw new NotFoundException("Không tìm thấy sân bóng nhỏ!");
        }

        if(!subPitchDto.getName().equals(updateSubPitchRequest.getName()) && subPitchDao.isExisting(updateSubPitchRequest.getName(), subPitchDto.getPitchId())){
            log.warn("Tên sân {} đã tồn tại trong sân bóng với id : {}",updateSubPitchRequest.getName(), subPitchDto.getPitchId());
            throw new InvalidDataException("Sân nhỏ với tên này đã tồn tại trong sân bóng!");
        }

        subPitchDto.setName(updateSubPitchRequest.getName());
        subPitchDto.setPitchTypeId(updateSubPitchRequest.getPitchTypeId());
        subPitchDto.setActive(updateSubPitchRequest.isActive());
        subPitchDto.setUpdateAt(LocalDateTime.now());

        subPitchDao.updateSubPitch(subPitchDto);
    }
}
