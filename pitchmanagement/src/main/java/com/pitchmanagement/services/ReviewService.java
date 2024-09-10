package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.review.CreateReviewRequest;
import com.pitchmanagement.models.requests.review.UpdateReviewRequest;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest createReviewRequest) throws Exception;
    PageResponse getAllByPitchId(Long pitchId, Long userId, int pageNumber, int limit, String orderSort);
    ReviewResponse updateReview(UpdateReviewRequest updateReviewRequest) throws Exception;
    void deleteReview(Long id);
}
