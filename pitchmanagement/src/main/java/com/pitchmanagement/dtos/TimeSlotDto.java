package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TimeSlotDto {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

}
