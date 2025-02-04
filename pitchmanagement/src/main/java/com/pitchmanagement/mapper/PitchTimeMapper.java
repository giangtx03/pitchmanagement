package com.pitchmanagement.mapper;


import com.pitchmanagement.dtos.requests.pitch.PitchTimeRequest;
import com.pitchmanagement.dtos.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.models.PitchTime;
import com.pitchmanagement.models.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PitchTimeMapper {

    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "timeSlotId", source = "timeSlot.id")
    PitchTime toPitchTime(TimeSlot timeSlot, Float price);

    @Mapping(target = "isActive", source = "active")
    PitchTimeResponse toPitchTimeResponse(PitchTime pitchTime);
}
