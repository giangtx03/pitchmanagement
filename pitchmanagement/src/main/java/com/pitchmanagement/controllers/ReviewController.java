package com.pitchmanagement.controllers;

import com.pitchmanagement.dtos.requests.review.CreateReviewRequest;
import com.pitchmanagement.dtos.requests.review.UpdateReviewRequest;
import com.pitchmanagement.dtos.responses.BaseResponse;
import com.pitchmanagement.dtos.responses.ReviewResponse;
import com.pitchmanagement.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createReview(
            @RequestBody @Valid CreateReviewRequest request
    ) throws Exception {

        ReviewResponse reviewResponse =  reviewService.createReview(request);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(reviewResponse)
                .message("Đánh giá thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping
    public ResponseEntity<BaseResponse> updeateReview(
            @RequestBody @Valid UpdateReviewRequest updateReviewRequest
    ) throws Exception {
        ReviewResponse reviewResponse =  reviewService.updateReview(updateReviewRequest);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .data(reviewResponse)
                .message("Cập nhật đánh giá thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteReview(
            @PathVariable("id") Long id
    ){
        reviewService.deleteReview(id);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa đánh giá thành công!")
                .build();
        return ResponseEntity.ok().body(response);
    }
}
