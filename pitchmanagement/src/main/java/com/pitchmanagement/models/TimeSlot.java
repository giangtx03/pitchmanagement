package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TimeSlot {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

}
