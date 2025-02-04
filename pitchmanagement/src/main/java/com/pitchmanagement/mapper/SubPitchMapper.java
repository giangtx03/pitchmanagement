package com.pitchmanagement.mapper;

import com.pitchmanagement.dtos.requests.pitch.SubPitchRequest;
import com.pitchmanagement.dtos.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.dtos.responses.pitch.SubPitchResponse;
import com.pitchmanagement.models.PitchTime;
import com.pitchmanagement.models.PitchType;
import com.pitchmanagement.models.SubPitch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SubPitchMapper {
    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isActive", constant = "true")
    SubPitch toSubPitch(SubPitchRequest request);

    @Mapping(target = "id", source = "subPitch.id")
    @Mapping(target = "createAt", source = "subPitch.createAt")
    @Mapping(target = "updateAt", source = "subPitch.updateAt")
    @Mapping(target = "isActive", source = "subPitch.active")
    @Mapping(target = "name", source = "subPitch.name")
    @Mapping(target = "pitchType", source = "pitchType")
    @Mapping(target = "pitchTimes", source = "pitchTimeResponses")
    SubPitchResponse toSubPitchResponse(SubPitch subPitch, String pitchType, List<PitchTimeResponse> pitchTimeResponses);

    default List<PitchTimeResponse> mapPitchTimes(List<PitchTime> pitchTimes, PitchTimeMapper pitchTimeMapper) {
        return pitchTimes.stream()
                .map(pitchTimeMapper::toPitchTimeResponse)
                .collect(Collectors.toList());
    }
}
