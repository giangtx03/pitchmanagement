package com.pitchmanagement.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImageDto {

    private Long id;
    private String name;
    private Long pitchId;

}
