package com.pitchmanagement.dtos.requests.sub_pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateSubPitchRequest {
    @JsonProperty("id")
    @Min(value = 1,message = "Id sân con không hợp lệ")
    private Long id;
    @JsonProperty("name")
    @NotBlank(message = "Tên sân con không được để trống")
    private String name;
    @JsonProperty("pitch_type_id")
    @Min(value = 1,message = "Id kiểu sân không hợp lệ")
    private Long pitchTypeId;
    @JsonProperty("is_active")
    @NotNull(message = "Trạng thái hoạt động không được null")
    private boolean isActive;
}
