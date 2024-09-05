package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.PitchDao;
import com.pitchmanagement.daos.PitchTypeDao;
import com.pitchmanagement.daos.SubPitchDao;
import com.pitchmanagement.dtos.PitchDto;
import com.pitchmanagement.dtos.PitchTypeDto;
import com.pitchmanagement.dtos.SubPitchDto;
import com.pitchmanagement.exceptions.InvalidDataException;
import com.pitchmanagement.models.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.models.requests.sub_pitch.UpdateSubPitchRequest;
import com.pitchmanagement.services.SubPitchService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubPitchServiceImpl implements SubPitchService {

    private final SubPitchDao subPitchDao;
    private final PitchTypeDao pitchTypeDao;
    private final PitchDao pitchDao;
    private final Logger logger = LoggerFactory.getLogger(SubPitchServiceImpl.class);
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void createSubPitch(CreateSubPitchRequest createSubPitchRequest) throws Exception {
        PitchDto pitchDto = pitchDao.getPitchById(createSubPitchRequest.getPitchId());
        PitchTypeDto pitchTypeDto = pitchTypeDao.getPitchTypeById(createSubPitchRequest.getPitchTypeId());

        if(pitchDto == null){
            logger.warn("Không tìm thấy sân bóng với id : {}", createSubPitchRequest.getPitchId());
            throw new NotFoundException("Không tìm thấy sân bóng");
        }
        if(pitchTypeDto == null){
            logger.warn("Không tìm thấy kiểu sân bóng với id : {}", createSubPitchRequest.getPitchTypeId());
            throw new NotFoundException("Không tìm thấy kiểu sân bóng");
        }

        if(subPitchDao.isExisting(createSubPitchRequest.getName(), createSubPitchRequest.getPitchId())){
            logger.warn("Tên sân {} đã tồn tại trong sân bóng : {}",createSubPitchRequest.getName(), pitchDto.getName());
            throw new InvalidDataException("Sân nhỏ với tên này đã tồn tại trong sân bóng!");
        }

        SubPitchDto subPitchDto = SubPitchDto.builder()
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
        SubPitchDto subPitchDto = subPitchDao.getSubPitchById(updateSubPitchRequest.getId());
        if(subPitchDto == null){
            throw new NotFoundException("Không tìm thấy sân bóng nhỏ!");
        }

        if(!subPitchDto.getName().equals(updateSubPitchRequest.getName()) && subPitchDao.isExisting(updateSubPitchRequest.getName(), subPitchDto.getPitchId())){
            logger.warn("Tên sân {} đã tồn tại trong sân bóng với id : {}",updateSubPitchRequest.getName(), subPitchDto.getPitchId());
            throw new InvalidDataException("Sân nhỏ với tên này đã tồn tại trong sân bóng!");
        }

        subPitchDto.setName(updateSubPitchRequest.getName());
        subPitchDto.setPitchTypeId(updateSubPitchRequest.getPitchTypeId());
        subPitchDto.setActive(updateSubPitchRequest.isActive());
        subPitchDto.setUpdateAt(LocalDateTime.now());

        subPitchDao.updateSubPitch(subPitchDto);
    }
}
