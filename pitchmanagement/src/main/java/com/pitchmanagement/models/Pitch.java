package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Pitch {

    private Long id;
    private String name;
    private String location;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long managerId;
    private User managerDto;
    private float avgStar;
    private boolean isActive;
}
