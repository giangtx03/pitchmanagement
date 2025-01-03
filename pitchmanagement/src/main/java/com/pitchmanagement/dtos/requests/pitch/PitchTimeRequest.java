package com.pitchmanagement.dtos.requests.pitch;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PitchTimeRequest {
    @Min(value = 1, message = "Khung thời gian không hợp lệ")
    private Long timeSlotId;

    @Min(value = 0, message = "Giá khung giờ và sân phải lớn hơn hoặc bằng 0")
    private Float price;
}
