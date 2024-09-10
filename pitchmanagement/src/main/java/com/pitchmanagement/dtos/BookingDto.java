package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingDto {
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
