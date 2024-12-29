package com.pitchmanagement.daos;

import com.pitchmanagement.models.Pitch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchDao {
    void insertPitch(Pitch pitch);
    List<Pitch> getAll(@Param("keyword") String keyword,
                       @Param("startPrice") int starPrice,
                       @Param("endPrice") int endPrice,
                       @Param("managerId") Long managerId,
                       @Param("starRange") int starRange,
                       @Param("pitchTypes") long[] pitchTypes,
                       @Param("requestQuery") boolean requestQuery);
    Pitch getPitchById(@Param("id") Long id, @Param("requestQuery") boolean requestQuery);
    void updatePitch(Pitch pitch);
}
