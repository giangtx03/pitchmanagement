package com.pitchmanagement.mapper;

import com.pitchmanagement.dtos.requests.pitch.CreatePitchRequest;
import com.pitchmanagement.dtos.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.dtos.responses.UserResponse;
import com.pitchmanagement.dtos.responses.pitch.PitchResponse;
import com.pitchmanagement.dtos.responses.pitch.SubPitchResponse;
import com.pitchmanagement.models.Pitch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PitchMapper {
    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isActive", constant = "true")
    Pitch toPitch(CreatePitchRequest request);

    @Mapping(target = "id", source = "pitch.id")
    @Mapping(target = "createAt", source = "pitch.createAt")
    @Mapping(target = "updateAt", source = "pitch.updateAt")
    @Mapping(target = "isActive", source = "pitch.active")
    @Mapping(target = "avgStar", source = "pitch.avgStar", defaultValue = "0")
    @Mapping(target = "manager", source = "managerResponse")
    @Mapping(target = "subPitches", source = "subPitchResponses")
    @Mapping(target = "images", source = "imageResponse")
    PitchResponse toPitchResponse(Pitch pitch, UserResponse managerResponse,
                                  List<SubPitchResponse> subPitchResponses, List<String> imageResponse);

    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    void updatePitch(UpdatePitchRequest request, @MappingTarget Pitch pitch);
}
