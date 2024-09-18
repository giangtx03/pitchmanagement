package com.pitchmanagement.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Long id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("note")
    private String note;
    @JsonProperty("deposit")
    private Float deposit;
    @JsonProperty("booking_date")
    private LocalDate bookingDate;
    @JsonProperty("user")
    private UserResponse user;
    @JsonProperty("pitch")
    private PitchResponse pitch;
    @JsonProperty("sub_pitch")
    private SubPitchResponse subPitch;
    @JsonProperty("pitch_time")
    private PitchTimeResponse pitchTime;
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @JsonProperty("update_at")
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
