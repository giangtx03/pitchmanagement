package com.pitchmanagement.controllers;

import com.pitchmanagement.models.requests.image.CreateImageRequest;
import com.pitchmanagement.models.requests.pitch.CreatePitchRequest;
import com.pitchmanagement.models.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;
import com.pitchmanagement.services.PitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/pitches")
public class PitchController {

    private final PitchService pitchService;


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> createPitch(
            @ModelAttribute @Valid CreatePitchRequest request
    ) throws Exception {
        PitchResponse pitchResponse = pitchService.createPitch(request);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(pitchResponse)
                .message("Tạo sân thành công!")
                .build();
        return ResponseEntity.ok().body(response);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<BaseResponse> updeatePitch(
            @RequestBody @Valid UpdatePitchRequest updatePitchRequest
    ) throws Exception {
        PitchResponse pitchResponse = pitchService.updatePitch(updatePitchRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(pitchResponse)
                .message("Cập nhật sân thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PostMapping(value = "/images",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> addImages(
            @ModelAttribute @Valid CreateImageRequest request
    ) throws Exception {

        pitchService.addImages(request);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Thêm hình ảnh thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @DeleteMapping("/images/{name}")
    public ResponseEntity<BaseResponse> deleteImage(
            @PathVariable("name") String name
    ){
        pitchService.deleteImage(name);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa hình ảnh thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }
}
