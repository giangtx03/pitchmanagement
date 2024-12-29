package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Booking {
    private Long id;
    private String status;
    private String note;
    private Float deposit;
    private LocalDate bookingDate;
    private Long userId;
    private Long subPitchId;
    private Long timeSlotId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
