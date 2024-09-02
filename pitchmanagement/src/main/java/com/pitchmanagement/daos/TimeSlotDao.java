package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.TimeSlotDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TimeSlotDao {

    void insertTimeSlot(TimeSlotDto timeSlotDto);
    List<TimeSlotDto> getAll();
    TimeSlotDto getTimeSlotById(@Param("id") Long id);

}
