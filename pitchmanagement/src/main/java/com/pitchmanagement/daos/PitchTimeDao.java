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
    void deletePitchTime(PitchTimeDto pitchTimeDto);

}
