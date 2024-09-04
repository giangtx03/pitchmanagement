package com.pitchmanagement.models.requests.pitch;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubPitchRequest {
    @NotBlank(message = "Tên sân con không được để trống")
    private String name;

    @Min(value = 1, message = "Loại sân phải khác rỗng")
    private Long pitchTypeId;

    private List<PitchTimeRequest> pitchTimes;
}
