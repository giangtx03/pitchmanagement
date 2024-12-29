package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.daos.PitchDao;
import com.pitchmanagement.daos.ReviewDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.models.Pitch;
import com.pitchmanagement.models.Review;
import com.pitchmanagement.models.User;
import com.pitchmanagement.dtos.requests.review.CreateReviewRequest;
import com.pitchmanagement.dtos.requests.review.UpdateReviewRequest;
import com.pitchmanagement.dtos.responses.PageResponse;
import com.pitchmanagement.dtos.responses.ReviewResponse;
import com.pitchmanagement.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final PitchDao pitchDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReviewResponse createReview(CreateReviewRequest createReviewRequest) throws Exception {
        Pitch pitch = Optional.ofNullable(pitchDao.getPitchById(createReviewRequest.getPitchId(),true))
                .orElseThrow(() -> new NotFoundException("Sân bóng không tồn tại!"));
        User userDto = Optional.ofNullable(userDao.getUserById(createReviewRequest.getUserId()))
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại!"));

        Review review = Review.builder()
                .pitchId(pitch.getId())
                .userId(userDto.getId())
                .comment(createReviewRequest.getComment())
                .star(createReviewRequest.getStar())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        reviewDao.insertComment(review);
        return ReviewResponse.fromReviewDtoAndUserResponse(review, userDto);
    }

    @Override
    public PageResponse getAllByPitchId(Long pitchId, Long userId,int star, int pageNumber, int limit, String orderSort) {
        PageHelper.startPage(pageNumber, limit);
        PageHelper.orderBy("create_at DESC, star " + orderSort);
        List<Review> reviewList = reviewDao.getAllByPitchId(pitchId, userId, star);
        PageInfo<Review> pageInfo = new PageInfo<>(reviewList);

        List<ReviewResponse> reviewResponseList = reviewList.stream().map(
                review -> {
                    User userDto = userDao.getUserById(review.getUserId());
                    return ReviewResponse.fromReviewDtoAndUserResponse(review, userDto);
                }
        ).toList();
        PageResponse responses = PageResponse.builder()
                .items(reviewResponseList)
                .totalPages(pageInfo.getPages())
                .totalItems(pageInfo.getTotal())
                .build();
        return responses;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReviewResponse updateReview(UpdateReviewRequest updateReviewRequest) throws Exception {
        User userDto = Optional.ofNullable(userDao.getUserById(updateReviewRequest.getUserId()))
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại!"));
        Review review = Optional.ofNullable(reviewDao.getReviewById(updateReviewRequest.getId()))
                .orElseThrow(() -> new NotFoundException("Đánh giá không tồn tại!"));

        if(!userDto.getId().equals(review.getUserId())){
            throw new AuthenticationException("Lỗi chủ sở hữu!");
        }

        review.setComment(updateReviewRequest.getComment());
        review.setStar(updateReviewRequest.getStar());
        review.setUpdateAt(LocalDateTime.now());

        reviewDao.updateComment(review);

        return ReviewResponse.fromReviewDtoAndUserResponse(review, userDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReview(Long id) {
        reviewDao.deleteComment(id);
    }
}
