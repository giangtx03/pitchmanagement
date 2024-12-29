package com.pitchmanagement.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Image {

    private Long id;
    private String name;
    private Long pitchId;

}
