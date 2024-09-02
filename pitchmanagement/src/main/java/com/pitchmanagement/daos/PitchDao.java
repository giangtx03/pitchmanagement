package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.PitchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchDao {
    void insertPitch(PitchDto pitchDto);
    List<PitchDto> getAll();
    PitchDto getPitchById(@Param("id") Long id);
    void updatePitch(PitchDto pitchDto);
}
