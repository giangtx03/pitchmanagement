package com.pitchmanagement.controllers.publics;

import com.pitchmanagement.constants.SortConstant;
import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.models.responses.PageResponse;
import com.pitchmanagement.services.ImageService;
import com.pitchmanagement.services.ReviewService;
import com.pitchmanagement.services.SendEmailService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/${api.prefix}")
public class AppController {

    private final ImageService imageService;
    private final ReviewService reviewService;

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/images/{image_name}")
    public ResponseEntity<?> getImage(
            @PathVariable("image_name") String imageName) {
        try {
            Resource source = imageService.download(imageName);
//            logger.info("Lấy hình ảnh : {}", imageName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(source);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/reviews/{pitch_id}")
    public ResponseEntity<?> getReview(
            @PathVariable("pitch_id") Long pitchId,
            @RequestParam(value = "user_id", defaultValue = "0") @Nullable Long userId,
            @RequestParam(value = "star", defaultValue = "0") @Nullable int star,
            @RequestParam(value = "page_number", defaultValue = "1") @Nullable int pageNumber,
            @RequestParam(value = "limit", defaultValue = "3") @Nullable int limit,
            @RequestParam(value = "order_sort", defaultValue = SortConstant.SORT_ASC) @Nullable String orderSort) {
        try {

            PageResponse pageResponse = reviewService.getAllByPitchId(pitchId,userId,star,pageNumber,limit,orderSort);

            BaseResponse response = BaseResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(pageResponse)
                    .message("Lấy danh sách sân thành công!")
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
