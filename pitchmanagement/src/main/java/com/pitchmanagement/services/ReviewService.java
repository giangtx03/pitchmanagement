package com.pitchmanagement.services;

import com.pitchmanagement.dtos.requests.review.CreateReviewRequest;
import com.pitchmanagement.dtos.requests.review.UpdateReviewRequest;
import com.pitchmanagement.dtos.responses.PageResponse;
import com.pitchmanagement.dtos.responses.ReviewResponse;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest createReviewRequest) throws Exception;
    PageResponse getAllByPitchId(Long pitchId, Long userId,int star, int pageNumber, int limit, String orderSort);
    ReviewResponse updateReview(UpdateReviewRequest updateReviewRequest) throws Exception;
    void deleteReview(Long id);
}
