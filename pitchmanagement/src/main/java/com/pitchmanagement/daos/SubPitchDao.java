package com.pitchmanagement.daos;

import com.pitchmanagement.models.SubPitch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubPitchDao {

    void insertSubPitch(SubPitch subPitchDto);
    List<SubPitch> getAllByPitchId(@Param("pitchId") Long pitchId, @Param("requestQuery") boolean requestQuery);
    void updateSubPitch(SubPitch subPitchDto);
    boolean isExisting(@Param("name") String name, @Param("pitchId") Long pitchId);
    SubPitch getSubPitchById(@Param("id") Long id);
}
