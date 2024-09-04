package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PitchTimeDto {

    private Long subPitchId;
    private Long timeSlotId;
    private Float price;
    private boolean isActive;
    private LocalTime startTime;
    private LocalTime endTime;

}
