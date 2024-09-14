package com.pitchmanagement.controllers;

import com.pitchmanagement.models.requests.payment.VNPayRequest;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.services.PaymentService;
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
@RequestMapping("public/${api.prefix}/payments")
public class PaymentController {

    private final PaymentService paymentService;
    @GetMapping("/create-payment")
    public ResponseEntity<BaseResponse> createPayment(
            @RequestBody @Valid VNPayRequest request,
            BindingResult result
    ) {
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
            String url = paymentService.createPayment(request.getPaymentType(), request.getUserId(), request.getBookingId());
            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(url)
                    .message("Tạo mẫu thanh toán thành công!")
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

    @GetMapping("/vnpay_return/{booking_id}")
    public ResponseEntity<BaseResponse> returnPayment(
        @PathVariable("booking_id") Long bookingId,
        @RequestParam("vnp_Amount") int amount,
        @RequestParam("vnp_BankCode") String bankCode,
        @RequestParam("vnp_OrderInfo") String orderInfo,
        @RequestParam("vnp_ResponseCode") String responseCode,
        @RequestParam(value = "vnp_TransactionStatus", required = false) String transactionStatus
    ) {
        try{
            paymentService.vnpayReturn(bookingId, amount, bankCode, orderInfo, responseCode, transactionStatus);
            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Thanh toán thành công!")
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
