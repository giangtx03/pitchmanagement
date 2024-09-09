package com.pitchmanagement.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
