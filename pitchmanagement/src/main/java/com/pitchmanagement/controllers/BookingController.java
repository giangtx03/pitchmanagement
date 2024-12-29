package com.pitchmanagement.controllers;

import com.pitchmanagement.dtos.requests.booking.CancelRequest;
import com.pitchmanagement.dtos.requests.booking.CreateBookingRequest;
import com.pitchmanagement.dtos.responses.BaseResponse;
import com.pitchmanagement.dtos.responses.BookingResponse;
import com.pitchmanagement.dtos.responses.PageResponse;
import com.pitchmanagement.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createBooking(
            @RequestBody @Valid CreateBookingRequest request
    ) throws Exception {

        BookingResponse bookingResponse = bookingService.createBooking(request);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(bookingResponse)
                .message("Đặt sân thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBookingById(
            @PathVariable("id") Long id
    ) throws Exception {
        BookingResponse bookingResponse = bookingService.getBookingById(id);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(bookingResponse)
                .message("Đơn đặt thành công!")
                .build();
        return ResponseEntity.ok().body(response);
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
        PageResponse pageResponse = bookingService.getAllByUserId(userId, keyword, pageNumber ,limit, status);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(pageResponse)
                .message("Lấy danh sách đặt thành công!")
                .build();
        return ResponseEntity.ok().body(response);
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
        PageResponse pageResponse = bookingService.getAllByManagerId(managerId,keyword, pageNumber ,limit, status);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(pageResponse)
                .message("Lấy danh sách đặt thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PostMapping("/cancel/{id}")
    public ResponseEntity<BaseResponse> cancelBooking(
            @PathVariable("id") Long bookingId,
            @RequestBody CancelRequest cancelRequest
    ) throws Exception {
        bookingService.requestCancelBooking(bookingId, cancelRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Yêu cầu hủy đơn thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }
}
