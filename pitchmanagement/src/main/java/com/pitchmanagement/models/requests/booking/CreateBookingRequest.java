package com.pitchmanagement.models.requests.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateBookingRequest {
    @JsonProperty("note")
    private String note;
    @JsonProperty("user_id")
    @Min(value = 1, message = "Người đặt sân không hợp lệ")
    private Long userId;
    @JsonProperty("sub_pitch_id")
    @Min(value = 1, message = "Sân bóng không hợp lệ")
    private Long sub_pitch_id;
    @JsonProperty("time_slot_id")
    @Min(value = 1, message = "Khung thời gian không hợp lệ")
    private Long timeSlotId;
}
