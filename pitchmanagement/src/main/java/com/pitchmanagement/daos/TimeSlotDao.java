package com.pitchmanagement.daos;

import com.pitchmanagement.models.TimeSlot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TimeSlotDao {

    void insertTimeSlot(TimeSlot timeSlotDto);
    List<TimeSlot> getAll();
    TimeSlot getTimeSlotById(@Param("id") Long id);

}
