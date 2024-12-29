package com.pitchmanagement.daos;

import com.pitchmanagement.models.PitchTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PitchTimeDao {

    void insertPitchTime(PitchTime pitchTime);
    List<PitchTime> getPitchTimeBySubPitchId(@Param("subPitchId") Long subPitchId, @Param("requestQuery") boolean requestQuery);
    void updatePitchTime(PitchTime pitchTime);
    boolean isExisting(@Param("subPitchId") Long subPitchId, @Param("timeSlotId") Long timeSlotId);
    PitchTime getBySubPitchIdAndTimeSlotId(@Param("subPitchId") Long subPitchId, @Param("timeSlotId") Long timeSlotId);
}
