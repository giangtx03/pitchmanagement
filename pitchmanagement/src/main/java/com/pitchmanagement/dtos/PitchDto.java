package com.pitchmanagement.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PitchDto {

    private Long id;
    private String name;
    private String location;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isActive;
    private Long managerId;

}
