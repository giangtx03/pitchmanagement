package com.pitchmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PitchTypeDto {

    private Long id;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
