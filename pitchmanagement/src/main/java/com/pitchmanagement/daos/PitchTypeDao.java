package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.PitchTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchTypeDao {

    List<PitchTypeDto> getAll();
    PitchTypeDto getPitchTypeById(@Param("id") Long id);

}
