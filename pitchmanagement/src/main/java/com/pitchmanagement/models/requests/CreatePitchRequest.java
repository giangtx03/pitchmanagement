package com.pitchmanagement.models.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePitchRequest {
    @NotBlank(message = "Tên sân không được để trống")
    private String name;
    @NotBlank(message = "Vị trí sân không được để trống")
    private String location;
    @Min(value = 1, message = "Quản lý sân phải khác rỗng")
    private Long managerId;
    private List<MultipartFile> images;
    private List<SubPitchRequest> subPitches;
}
