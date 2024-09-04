package com.pitchmanagement.models.responses.pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PitchTimeResponse {
    @JsonProperty("price")
    private Float price;
    @JsonProperty("start_time")
    private LocalTime startTime;
    @JsonProperty("end_time")
    private LocalTime endTime;
    @JsonProperty("is_active")
    private boolean isActive;
}
