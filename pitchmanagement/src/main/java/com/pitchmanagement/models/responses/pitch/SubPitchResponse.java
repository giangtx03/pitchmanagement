package com.pitchmanagement.models.responses.pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SubPitchResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("update_at")
    private LocalDateTime updateAt;
    @JsonProperty("pitch_times")
    private List<PitchTimeResponse> pitchTimes;
    @JsonProperty("pitch_type")
    private String pitchType;
    @JsonProperty("is_active")
    private boolean isActive;
}
