package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.PitchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchDao {
    void insertPitch(PitchDto pitchDto);
    List<PitchDto> getAll(@Param("keyword") String keyword,
                          @Param("managerId") Long managerId,
                          @Param("startPrice") int starPrice,
                          @Param("endPrice") int endPrice,
                          @Param("starRange") int starRange,
                          @Param("pitchTypes") long[] pitchTypes);
    PitchDto getPitchById(@Param("id") Long id);
    void updatePitch(PitchDto pitchDto);
}
