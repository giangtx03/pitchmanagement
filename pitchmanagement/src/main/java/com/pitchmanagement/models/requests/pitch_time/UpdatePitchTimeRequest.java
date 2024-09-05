package com.pitchmanagement.models.requests.pitch_time;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdatePitchTimeRequest {
    @JsonProperty("price")
    @Min(value = 0,message = "Giá khung giờ không hợp lệ")
    private Float price;
    @JsonProperty("time_slot_id")
    @Min(value = 1,message = "Id khung thời gian không hợp lệ")
    private Long timeSlotId;
    @JsonProperty("sub_pitch_id")
    @Min(value = 1,message = "Id sân con không hợp lệ")
    private Long subPitchId;
    @JsonProperty("is_active")
    @NotNull(message = "Trạng thái hoạt động không được null")
    private boolean isActive;
}
