package com.pitchmanagement.models.requests.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateReviewRequest {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("user_id")
    @Min(value = 1, message = "Người đánh giá sân không hợp lệ")
    private Long userId;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("star")
    @Min(value = 1, message = "Đánh giá tối thiểu 1 sao")
    private int star;
}
