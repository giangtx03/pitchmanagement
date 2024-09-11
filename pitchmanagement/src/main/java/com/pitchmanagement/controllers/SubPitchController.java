package com.pitchmanagement.controllers;

import com.pitchmanagement.models.requests.sub_pitch.CreateSubPitchRequest;
import com.pitchmanagement.models.requests.sub_pitch.UpdateSubPitchRequest;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.services.SubPitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/${api.prefix}/sub-pitches")
public class SubPitchController {

    private final SubPitchService subPitchService;

    @PostMapping
    public ResponseEntity<BaseResponse> createSubPitch(
            @RequestBody @Valid CreateSubPitchRequest request,
            BindingResult result
    ){
        if (result.hasErrors()) {
            // lấy ra danh sách lỗi
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // trả về danh sách lỗi
            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(errorMessages.toString())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        try{
            subPitchService.createSubPitch(request);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Tạo sân con thành công!")
                    .build();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @PutMapping
    public ResponseEntity<BaseResponse> updeateSubPitch(
            @RequestBody @Valid UpdateSubPitchRequest updateSubPitchRequest,
            BindingResult result
    ){
        if (result.hasErrors()) {
            // lấy ra danh sách lỗi
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            // trả về danh sách lỗi
            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(errorMessages.toString())
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        try{
            subPitchService.updateSubPitch(updateSubPitchRequest);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Cập nhật sân con thành công!")
                    .build();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
