package com.pitchmanagement.dtos.requests.image;

import jakarta.validation.constraints.Min;
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
public class CreateImageRequest {
    @Min(value = 1, message = "Id sân bóng không hợp lệ")
    private Long pitchId;
    private List<MultipartFile> images;
}
