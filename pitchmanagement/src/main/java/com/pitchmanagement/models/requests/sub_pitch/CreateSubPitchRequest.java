package com.pitchmanagement.models.requests.sub_pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateSubPitchRequest {
    @JsonProperty("name")
    @NotBlank(message = "Tên sân con không được để trống")
    private String name;
    @JsonProperty("pitch_type_id")
    @Min(value = 1,message = "Id kiểu sân không hợp lệ")
    private Long pitchTypeId;
    @JsonProperty("pitch_id")
    @Min(value = 1,message = "Id sân sân không hợp lệ")
    private Long pitchId;
}
