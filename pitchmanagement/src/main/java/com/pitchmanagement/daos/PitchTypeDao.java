package com.pitchmanagement.daos;

import com.pitchmanagement.models.PitchType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchTypeDao {

    List<PitchType> getAll();
    PitchType getPitchTypeById(@Param("id") Long id);

}
