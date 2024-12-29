package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PitchTime {

    private Long subPitchId;
    private Long timeSlotId;
    private Float price;
    private boolean isActive;
    private LocalTime startTime;
    private LocalTime endTime;

}
