package com.pitchmanagement.dtos.responses.pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pitchmanagement.dtos.responses.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PitchResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("location")
    private String location;
    @JsonProperty("manager")
    private UserResponse manager;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("update_at")
    private LocalDateTime updateAt;
    @JsonProperty("sub_pitches")
    private List<SubPitchResponse> subPitches;
    @JsonProperty("images")
    private List<String> images;
    @JsonProperty("is_active")
    private boolean isActive;
    @JsonProperty("avg_star")
    private float avgStar;
}
