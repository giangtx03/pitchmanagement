package com.pitchmanagement.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PageResponse {

    @JsonProperty("items")
    private Object items;

    @JsonProperty("total_items")
    private Long totalItems;

    @JsonProperty("total_pages")
    private int totalPages;

}
