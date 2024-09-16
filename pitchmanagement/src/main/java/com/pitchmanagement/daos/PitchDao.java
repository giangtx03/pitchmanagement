package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.PitchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchDao {
    void insertPitch(PitchDto pitchDto);
    List<PitchDto> getAll(@Param("keyword") String keyword,
                          @Param("startPrice") int starPrice,
                          @Param("endPrice") int endPrice,
                          @Param("managerId") Long managerId,
                          @Param("starRange") int starRange,
                          @Param("pitchTypes") long[] pitchTypes,
                          @Param("requestQuery") boolean requestQuery);
    PitchDto getPitchById(@Param("id") Long id, @Param("requestQuery") boolean requestQuery);
    void updatePitch(PitchDto pitchDto);
}
