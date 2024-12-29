package com.pitchmanagement.daos;

import com.pitchmanagement.models.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewDao {
    void insertComment(Review review);
    List<Review> getAllByPitchId(@Param("pitchId") Long pitchId, @Param("userId") Long userId, @Param("star") int star);
    Review getReviewById(@Param("id") Long id);
    void updateComment(Review review);
    void deleteComment(@Param("id") Long id);
}
