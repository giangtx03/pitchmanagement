package com.pitchmanagement.dtos.requests.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateBookingRequest {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("status")
    private String status;
}
