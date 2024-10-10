package com.pitchmanagement.controllers.publics;

import com.pitchmanagement.constants.SortConstant;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.models.responses.pitch.PitchResponse;
import com.pitchmanagement.services.PitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/${api.prefix}/pitches")
public class PublicPitchController {

    private final PitchService pitchService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAll(
            @RequestParam(value = "keyword", required = false) @Nullable String keyword,
            @RequestParam(value = "page_number", defaultValue = "1") @Nullable int pageNumber,
            @RequestParam(value = "limit", defaultValue = "12") @Nullable int limit,
            @RequestParam(value = "start_price", defaultValue = "0") @Nullable int startPrice,
            @RequestParam(value = "end_price", defaultValue = "0") @Nullable int endPrice,
            @RequestParam(value = "manager_id", defaultValue = "0") @Nullable Long managerId,
            @RequestParam(value = "star_range", defaultValue = "0") @Nullable int starRange,
            @RequestParam(value = "pitch_types", defaultValue = "") long[] pitchTypes,
            @RequestParam(value = "request_query")@Nullable boolean requestQuery,
            @RequestParam(value = "order_by", defaultValue = SortConstant.SORT_BY_AVG_STAR) @Nullable String orderBy,
            @RequestParam(value = "order_sort", defaultValue = SortConstant.SORT_DESC) @Nullable String orderSort
    ){
        try {

            PageResponse pageResponse = pitchService.getAll(keyword,
                    startPrice, endPrice,managerId,  starRange, pitchTypes, requestQuery,
                    pageNumber, limit, orderBy, orderSort);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(pageResponse)
                    .message("Lấy danh sách sân thành công!")
                    .build();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPitchById(@PathVariable Long id, @RequestParam(value = "request_query")@Nullable boolean requestQuery){
        try {

            PitchResponse pitchResponse = pitchService.getPitchById(id, requestQuery);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(pitchResponse)
                    .message("Lấy chi tiết sân thành công!")
                    .build();
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
