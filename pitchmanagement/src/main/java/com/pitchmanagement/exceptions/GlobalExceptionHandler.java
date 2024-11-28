package com.pitchmanagement.exceptions;

import static org.springframework.http.HttpStatus.*;
import java.util.List;

import com.pitchmanagement.models.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationException(MethodArgumentNotValidException exception){

        List<String> errorMessages = exception.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessages.toString())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception exception){
        return ResponseEntity.badRequest()
                .body(BaseResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<BaseResponse> handleInvalidException(InvalidDataException exception){
        return ResponseEntity.badRequest()
                .body(BaseResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(exception.getMessage())
                        .build());
    }
}
