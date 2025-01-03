package com.pitchmanagement.models;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SubPitch {

    private Long id;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean isActive;
    private Long pitchId;
    private Long pitchTypeId;
    private String pitchType;

}
