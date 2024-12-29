package com.pitchmanagement.dtos.responses.pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

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
    @JsonProperty("schedules")
    private List<String> schedules;
    @JsonProperty("time_slot_id")
    private Long timeSlotId;
}
