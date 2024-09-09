package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewDto {

    private Long id;
    private Long pitchId;
    private Long userId;
    private int star;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
