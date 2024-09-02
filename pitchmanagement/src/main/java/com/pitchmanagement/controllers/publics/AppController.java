package com.pitchmanagement.controllers.publics;

import com.pitchmanagement.models.responses.BaseResponse;
import com.pitchmanagement.services.ImageService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/${api.prefix}")
public class AppController {

    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/images/{image_name}")
    public ResponseEntity<?> getImage(
            @PathVariable("image_name") String imageName) {
        try {
            Resource source = imageService.download(imageName);
            logger.info("Lấy hình ảnh : {}", imageName);
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
}
