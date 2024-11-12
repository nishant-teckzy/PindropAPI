package com.novelvox.nbh.genesys.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class GenesysExceptionAdvice {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignStatusException(FeignException e, WebRequest request) {
        log.error("FeignException caught: Status code: {}, Reason: {}, Message: {}",
                e.status(), e.getMessage(), e.contentUTF8());
        return ResponseEntity.status(e.status())
                .body(e.getMessage());
    }

}
