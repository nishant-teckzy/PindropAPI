package com.novelvox.nbh.pindrop.client;

import feign.FeignException;
import feign.RetryableException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class PindropExceptionAdvice {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignStatusException(FeignException e, WebRequest request) {
        log.error("FeignException caught: Status code: {}, Reason: {}, Message: {}",
                e.status(), e.getMessage(), e.contentUTF8());

        return ResponseEntity.status(e.status())
                .body(e.getMessage());
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleRetryableException(RetryableException e, WebRequest request) {
        log.error("RetryableException caught: Status code: {}, Reason: {}, Message: {}",
                e.status(), e.getMessage(), e.getMessage());
        return ResponseEntity.status(e.status())
                .body(e.getMessage());
    }

}
