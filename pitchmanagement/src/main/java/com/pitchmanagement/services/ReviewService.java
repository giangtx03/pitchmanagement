package com.pitchmanagement.services;

import com.pitchmanagement.models.requests.review.CreateReviewRequest;
import com.pitchmanagement.models.responses.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest request) throws Exception;
    List<ReviewResponse> getAllByPitchId(Long pitchId, Long userId, String sortOrder);

}
