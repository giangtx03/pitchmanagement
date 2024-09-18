package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.SubPitchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubPitchDao {

    void insertSubPitch(SubPitchDto subPitchDto);
    List<SubPitchDto> getAllByPitchId(@Param("pitchId") Long pitchId, @Param("requestQuery") boolean requestQuery);
    void updateSubPitch(SubPitchDto subPitchDto);
    boolean isExisting(@Param("name") String name, @Param("pitchId") Long pitchId);
    SubPitchDto getSubPitchById(@Param("id") Long id);
}
