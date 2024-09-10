package com.pitchmanagement.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pitchmanagement.dtos.ReviewDto;
import com.pitchmanagement.dtos.UserDto;
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

    public static ReviewResponse fromReviewDtoAndUserResponse(ReviewDto reviewDto, UserDto userDto){
        return  ReviewResponse.builder()
                .id(reviewDto.getId())
                .pitchId(reviewDto.getPitchId())
                .user(UserResponse.fromUserDto(userDto))
                .comment(reviewDto.getComment())
                .star(reviewDto.getStar())
                .createAt(reviewDto.getCreateAt())
                .updateAt(reviewDto.getUpdateAt())
                .build();
    }
}
