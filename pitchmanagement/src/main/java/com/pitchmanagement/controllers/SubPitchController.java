package com.pitchmanagement.controllers;

import com.pitchmanagement.dtos.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.dtos.requests.sub_pitch.UpdateSubPitchRequest;
import com.pitchmanagement.dtos.responses.BaseResponse;
import com.pitchmanagement.services.SubPitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/sub-pitches")
public class SubPitchController {

    private final SubPitchService subPitchService;


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createSubPitch(
            @RequestBody @Valid CreateSubPitchRequest request
    ) throws Exception {
        subPitchService.createSubPitch(request);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo sân con thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<BaseResponse> updeateSubPitch(
            @RequestBody @Valid UpdateSubPitchRequest updateSubPitchRequest
    ) throws Exception {
        subPitchService.updateSubPitch(updateSubPitchRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật sân con thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }
}
