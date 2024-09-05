package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.PitchTimeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchTimeDao {

    void insertPitchTime(PitchTimeDto pitchTimeDto);
    List<PitchTimeDto> getPitchTimeBySubPitchId(@Param("subPitchId") Long subPitchId);
    void updatePitchTime(PitchTimeDto pitchTimeDto);
    boolean isExisting(@Param("subPitchId") Long subPitchId, @Param("timeSlotId") Long timeSlotId);
    PitchTimeDto getBySubPitchIdAndTimeSlotId(@Param("subPitchId") Long subPitchId, @Param("timeSlotId") Long timeSlotId);
}
