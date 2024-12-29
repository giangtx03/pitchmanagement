package com.pitchmanagement.dtos.requests.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CancelRequest {
    @JsonProperty("case_cancel")
    @NotNull
    private String caseCancel;
    @JsonProperty("note")
    private String note;
}
