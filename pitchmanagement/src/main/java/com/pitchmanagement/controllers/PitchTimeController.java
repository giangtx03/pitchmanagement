package com.pitchmanagement.controllers;

import com.pitchmanagement.models.requests.pitch_time.CreatePitchTimeRequest;
import com.pitchmanagement.models.requests.pitch_time.UpdatePitchTimeRequest;
import com.pitchmanagement.models.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.models.requests.sub_pitch.UpdateSubPitchRequest;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.services.PitchTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/pitch-times")
public class PitchTimeController {

    private final PitchTimeService pitchTimeService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createPitchTime(
            @RequestBody @Valid CreatePitchTimeRequest request
    ) throws Exception {
        pitchTimeService.createPitchTime(request);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Thêm khung thời gian thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<BaseResponse> updeatePitchTime(
            @RequestBody @Valid UpdatePitchTimeRequest updatePitchTimeRequest
    ) throws Exception {
        pitchTimeService.updatePitchTime(updatePitchTimeRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật khung thời gian thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

}
