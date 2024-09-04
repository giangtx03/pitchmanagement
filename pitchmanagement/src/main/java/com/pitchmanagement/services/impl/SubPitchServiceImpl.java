package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.SubPitchDao;
import com.pitchmanagement.dtos.SubPitchDto;
import com.pitchmanagement.exceptions.InvalidDataException;
import com.pitchmanagement.models.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.models.requests.sub_pitch.UpdateSubPitchRequest;
import com.pitchmanagement.services.SubPitchService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubPitchServiceImpl implements SubPitchService {

    private final SubPitchDao subPitchDao;

    @Override
    public void createSubPitch(CreateSubPitchRequest createSubPitchRequest) throws Exception {
        if(subPitchDao.isExisting(createSubPitchRequest.getName(), createSubPitchRequest.getPitchId())){
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
    public void updateSubPitch(UpdateSubPitchRequest updateSubPitchRequest) throws Exception {
        SubPitchDto subPitchDto = subPitchDao.getSubPitchById(updateSubPitchRequest.getId());
        if(subPitchDto == null){
            throw new NotFoundException("Không tìm thấy sân bóng nhỏ!");
        }

        if(subPitchDao.isExisting(updateSubPitchRequest.getName(), subPitchDto.getPitchId())){
            throw new InvalidDataException("Sân nhỏ với tên này đã tồn tại trong sân bóng!");
        }

        subPitchDto.setName(updateSubPitchRequest.getName());
        subPitchDto.setPitchTypeId(updateSubPitchRequest.getPitchTypeId());
        subPitchDto.setActive(updateSubPitchRequest.isActive());
        subPitchDto.setUpdateAt(LocalDateTime.now());

        subPitchDao.updateSubPitch(subPitchDto);
    }
}
