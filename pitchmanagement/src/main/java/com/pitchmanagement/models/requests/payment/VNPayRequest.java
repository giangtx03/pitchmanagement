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
    @JsonProperty("amount")
    @Min(value = 1, message = "Số tiền thanh toán phải lớn hơn 1")
    private int amount;
    @JsonProperty("payment_type")
    @NotNull(message = "Kiểu thanh toán không được null")
    private String paymentType;
    @JsonProperty("user_id")
    @Min(value = 1, message = "Người thanh toán không hợp lệ")
    private Long userId;
    @JsonProperty("booking_id")
    @Min(value = 1, message = "Đơn đặt không hợp lệ")
    private Long bookingId;
}
