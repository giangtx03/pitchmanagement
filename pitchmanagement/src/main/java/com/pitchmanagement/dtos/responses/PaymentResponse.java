package com.pitchmanagement.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PaymentResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("amount")
    private Float amount;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("note")
    private String note;
    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("booking")
    private BookingResponse bookingResponse;
}
