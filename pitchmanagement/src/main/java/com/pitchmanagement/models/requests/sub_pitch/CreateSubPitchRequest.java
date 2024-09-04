package com.pitchmanagement.models.requests.sub_pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateSubPitchRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("pitch_type_id")
    private Long pitchTypeId;
    @JsonProperty("pitch_id")
    private Long pitchId;
}
