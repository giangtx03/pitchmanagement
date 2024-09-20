package com.pitchmanagement.models.requests.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VNPayRequest {
    @JsonProperty("payment_type")
    @NotNull(message = "Kiểu thanh toán không được null")
    private String paymentType;
    @JsonProperty("booking_id")
    @Min(value = 1, message = "Đơn đặt không hợp lệ")
    private Long bookingId;
    @JsonProperty("note")
    private String note;
    @JsonProperty("amount")
    @Min(value = 0, message = "Tiền thanh toán không hợp lệ")
    private Float amount;
}
