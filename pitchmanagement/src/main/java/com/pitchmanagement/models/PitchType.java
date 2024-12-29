package com.pitchmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PitchType {

    private Long id;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
