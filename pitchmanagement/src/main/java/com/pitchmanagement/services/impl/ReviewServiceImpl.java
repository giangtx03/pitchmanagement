package com.pitchmanagement.services.impl;

import com.pitchmanagement.daos.PitchDao;
import com.pitchmanagement.daos.ReviewDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.models.requests.review.CreateReviewRequest;
import com.pitchmanagement.models.responses.ReviewResponse;
import com.pitchmanagement.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final PitchDao pitchDao;
    @Override
    public ReviewResponse createReview(CreateReviewRequest request) throws Exception {
        return null;
    }

    @Override
    public List<ReviewResponse> getAllByPitchId(Long pitchId, Long userId, String sortOrder) {
        return null;
    }
}
