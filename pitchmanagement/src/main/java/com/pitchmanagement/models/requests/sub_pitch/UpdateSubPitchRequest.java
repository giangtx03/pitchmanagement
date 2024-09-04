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
public class UpdateSubPitchRequest {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("pitch_type_id")
    private Long pitchTypeId;
    @JsonProperty("is_active")
    private boolean isActive;
}
