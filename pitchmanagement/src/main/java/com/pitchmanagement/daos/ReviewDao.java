package com.pitchmanagement.daos;

import com.pitchmanagement.dtos.ReviewDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewDao {
    void insertComment(ReviewDto reviewDto);
    List<ReviewDto> getAllByPitchId(@Param("pitchId") Long pitchId, @Param("userId") Long userId);
    void updateComment(ReviewDto reviewDto);
    void deleteComment(@Param("id") Long id);
}
