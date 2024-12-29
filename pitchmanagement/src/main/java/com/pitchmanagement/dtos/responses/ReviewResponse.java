package com.pitchmanagement.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pitchmanagement.models.Review;
import com.pitchmanagement.models.User;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("pitch_id")
    private Long pitchId;
    @JsonProperty("user")
    private UserResponse user;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("star")
    private int star;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("update_at")
    private LocalDateTime updateAt;

    public static ReviewResponse fromReviewDtoAndUserResponse(Review review, User userDto){
        return  ReviewResponse.builder()
                .id(review.getId())
                .pitchId(review.getPitchId())
                .user(UserResponse.fromUserDto(userDto))
                .comment(review.getComment())
                .star(review.getStar())
                .createAt(review.getCreateAt())
                .updateAt(review.getUpdateAt())
                .build();
    }
}
