package com.pitchmanagement.models.responses;

import com.pitchmanagement.dtos.*;
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
    private PitchResponse pitch;
    private SubPitchResponse subPitch;
    private PitchTimeResponse pitchTime;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static BookingResponse toBookingResponse(BookingDto bookingDto, UserResponse userResponse,
                                                    PitchResponse pitchResponse, SubPitchResponse subPitchResponse,
                                                    PitchTimeResponse pitchTimeResponse){
        return BookingResponse.builder()
                .id(bookingDto.getId())
                .status(bookingDto.getStatus())
                .note(bookingDto.getNote())
                .deposit(bookingDto.getDeposit())
                .bookingDate(bookingDto.getBookingDate())
                .user(userResponse)
                .pitch(pitchResponse)
                .subPitch(subPitchResponse)
                .pitchTime(pitchTimeResponse)
                .createAt(bookingDto.getCreateAt())
                .updateAt(bookingDto.getUpdateAt())
                .build();
    }
}
