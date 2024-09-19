package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PaymentDto {
    private Long id;
    private String note;
    private Float amount;
    private String paymentMethod;
    private String paymentType;
    private Long userId;
    private Long bookingId;
    private Long managerId;
    private LocalDateTime createAt;
}
