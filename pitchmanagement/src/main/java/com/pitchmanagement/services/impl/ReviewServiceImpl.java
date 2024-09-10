package com.pitchmanagement.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pitchmanagement.daos.PitchDao;
import com.pitchmanagement.daos.ReviewDao;
import com.pitchmanagement.daos.UserDao;
import com.pitchmanagement.dtos.PitchDto;
import com.pitchmanagement.dtos.ReviewDto;
import com.pitchmanagement.dtos.UserDto;
import com.pitchmanagement.models.requests.review.CreateReviewRequest;
import com.pitchmanagement.models.requests.review.UpdateReviewRequest;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.ReviewResponse;
import com.pitchmanagement.models.responses.UserResponse;
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
        PitchDto pitchDto = Optional.ofNullable(pitchDao.getPitchById(createReviewRequest.getPitchId()))
                .orElseThrow(() -> new NotFoundException("Sân bóng không tồn tại!"));
        UserDto userDto = Optional.ofNullable(userDao.getUserById(createReviewRequest.getUserId()))
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại!"));

        ReviewDto reviewDto = ReviewDto.builder()
                .pitchId(pitchDto.getId())
                .userId(userDto.getId())
                .comment(createReviewRequest.getComment())
                .star(createReviewRequest.getStar())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        reviewDao.insertComment(reviewDto);
        return ReviewResponse.fromReviewDtoAndUserResponse(reviewDto, userDto);
    }

    @Override
    public PageResponse getAllByPitchId(Long pitchId, Long userId, int pageNumber, int limit, String orderSort) {
        PageHelper.startPage(pageNumber, limit);
        PageHelper.orderBy("create_at DESC, star " + orderSort);
        List<ReviewDto> reviewDtoList = reviewDao.getAllByPitchId(pitchId, userId);
        PageInfo<ReviewDto> pageInfo = new PageInfo<>(reviewDtoList);

        List<ReviewResponse> reviewResponseList = reviewDtoList.stream().map(
                reviewDto -> {
                    UserDto userDto = userDao.getUserById(reviewDto.getUserId());
                    return ReviewResponse.fromReviewDtoAndUserResponse(reviewDto, userDto);
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
        UserDto userDto = Optional.ofNullable(userDao.getUserById(updateReviewRequest.getUserId()))
                .orElseThrow(() -> new NotFoundException("Người dùng không tồn tại!"));
        ReviewDto reviewDto = Optional.ofNullable(reviewDao.getReviewById(updateReviewRequest.getId()))
                .orElseThrow(() -> new NotFoundException("Đánh giá không tồn tại!"));

        if(!userDto.getId().equals(reviewDto.getUserId())){
            throw new AuthenticationException("Lỗi chủ sở hữu!");
        }

        reviewDto.setComment(updateReviewRequest.getComment());
        reviewDto.setStar(updateReviewRequest.getStar());
        reviewDto.setUpdateAt(LocalDateTime.now());

        reviewDao.updateComment(reviewDto);

        return ReviewResponse.fromReviewDtoAndUserResponse(reviewDto, userDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReview(Long id) {
        reviewDao.deleteComment(id);
    }
}
