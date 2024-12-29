package com.pitchmanagement.dtos.requests.pitch;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePitchRequest {
    @JsonProperty("id")
    @Min(value = 1, message = "Id sân không hợp lệ")
    private Long id;
    @JsonProperty("name")
    @NotBlank(message = "Tên sân không được để trống")
    private String name;
    @JsonProperty("location")
    @NotBlank(message = "Vị trí sân không được để trống")
    private String location;
    @JsonProperty("manager_id")
    @Min(value = 1, message = "Quản lý sân không hợp lệ")
    private Long managerId;
    @JsonProperty("is_active")
    private boolean isActive;
}
