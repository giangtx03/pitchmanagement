package com.pitchmanagement.controllers.publics;

import com.pitchmanagement.constants.SortConstant;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.services.*;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/${api.prefix}")
public class AppController {

    private final ImageService imageService;
    private final ReviewService reviewService;
    private final PaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/images/{image_name}")
    public ResponseEntity<?> getImage(
            @PathVariable("image_name") String imageName
    ) throws Exception {
        Resource source = imageService.download(imageName);
//            logger.info("Lấy hình ảnh : {}", imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(source);
    }

    @GetMapping("/reviews/{pitch_id}")
    public ResponseEntity<?> getReview(
            @PathVariable("pitch_id") Long pitchId,
            @RequestParam(value = "user_id", defaultValue = "0") @Nullable Long userId,
            @RequestParam(value = "star", defaultValue = "0") @Nullable int star,
            @RequestParam(value = "page_number", defaultValue = "1") @Nullable int pageNumber,
            @RequestParam(value = "limit", defaultValue = "3") @Nullable int limit,
            @RequestParam(value = "order_sort", defaultValue = SortConstant.SORT_ASC) @Nullable String orderSort
    ) {
        PageResponse pageResponse = reviewService.getAllByPitchId(pitchId,userId,star,pageNumber,limit,orderSort);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(pageResponse)
                .message("Lấy danh sách sân thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/payments/vnpay-return")
    public RedirectView returnPayment(
            @RequestParam("booking_id") Long bookingId,
            @RequestParam("vnp_Amount") int amount,
            @RequestParam("vnp_BankCode") String bankCode,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("note") String note,
            @RequestParam("payment_type") String paymentType,
            @RequestParam(value = "vnp_TransactionStatus", required = false) String transactionStatus
    ) throws UnsupportedEncodingException {
        try{
            paymentService.vnpayReturn(bookingId, amount, note,paymentType, bankCode, orderInfo, responseCode, transactionStatus);
            String encodedMessage = URLEncoder.encode("Thanh toán thành công!", StandardCharsets.UTF_8.toString());
            String redirectUrl = "http://localhost:3000/users/bookings?" + "message=" + encodedMessage;
            return new RedirectView(redirectUrl);
        }
        catch (Exception e){
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8.toString());
            String errorUrl = "http://localhost:3000/users/bookings?" + "message=" + encodedMessage;
            return new RedirectView(errorUrl);
        }
    }
}
