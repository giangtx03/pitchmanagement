package com.pitchmanagement.models.responses;

import com.pitchmanagement.models.responses.pitch.PitchResponse;
import com.pitchmanagement.models.responses.pitch.PitchTimeResponse;
import com.pitchmanagement.models.responses.pitch.SubPitchResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookingResponse {
    private Long id;
    private String status;
    private String note;
    private Float deposit;
    private LocalDate bookingDate;
    private UserResponse user;
    private PitchResponse pitchResponse;
    private SubPitchResponse subPitchResponse;
    private PitchTimeResponse pitchTimeResponse;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
