package com.pitchmanagement.controllers;

import com.pitchmanagement.constants.SortConstant;
import com.pitchmanagement.models.requests.booking.CreateBookingRequest;
import com.pitchmanagement.models.requests.booking.UpdateBookingRequest;
import com.pitchmanagement.models.requests.pitch.UpdatePitchRequest;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.BookingResponse;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;
import com.pitchmanagement.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createBooking(
            @RequestBody @Valid CreateBookingRequest request,
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
            BookingResponse bookingResponse = bookingService.createBooking(request);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .data(bookingResponse)
                    .message("Đặt sân thành công!")
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


    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBookingById(
            @PathVariable("id") Long id
    ){
        try {

            BookingResponse bookingResponse = bookingService.getBookingById(id);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(bookingResponse)
                    .message("Đơn đặt thành công!")
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

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user/{user_id}")
    public ResponseEntity<BaseResponse> getBookingByUserId(
            @PathVariable("user_id") Long userId,
            @RequestParam(value = "keyword", defaultValue = "") @Nullable String keyword,
            @RequestParam(value = "page_number", defaultValue = "1") @Nullable int pageNumber,
            @RequestParam(value = "limit", defaultValue = "12") @Nullable int limit,
            @RequestParam(value = "status", defaultValue = "") @Nullable String status
    ){
        try {

            PageResponse pageResponse = bookingService.getAllByUserId(userId, keyword, pageNumber ,limit, status);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(pageResponse)
                    .message("Lấy danh sách đặt thành công!")
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


    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @GetMapping("/manager/{manager_id}")
    public ResponseEntity<BaseResponse> getBookingByManagerId(
            @PathVariable("manager_id") Long managerId,
            @RequestParam(value = "keyword", defaultValue = "") @Nullable String keyword,
            @RequestParam(value = "page_number", defaultValue = "1") @Nullable int pageNumber,
            @RequestParam(value = "limit", defaultValue = "12") @Nullable int limit,
            @RequestParam(value = "status") @Nullable String status
    ){
        try {

            PageResponse pageResponse = bookingService.getAllByManagerId(managerId,keyword, pageNumber ,limit, status);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(pageResponse)
                    .message("Lấy danh sách đặt thành công!")
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
