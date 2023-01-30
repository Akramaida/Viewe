package com.project.viewe.exception;

import com.project.viewe.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeInMB;

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(ProjectException exception) {
        ErrorResponse err = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(exception.getMessage()).build();
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException() {
        ErrorResponse err = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message("Maximum upload size of " + maxSizeInMB + " exceeded").build();
        log.error("Maximum upload size of " + maxSizeInMB + " exceeded");
        return new ResponseEntity<>(err, HttpStatus.PAYLOAD_TOO_LARGE);
    }
}
