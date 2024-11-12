package com.novelvox.nbh.pindrop.client.configs;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.loadbalancer.RetryableStatusCodeException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Log4j2
public class FeignErrDecoder implements ErrorDecoder {
    private static final String TAG = "PindropAPI >> Status:{},Reason:{},Message:{}";
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        if(response.status() == 401) return exception;
        System.out.println("Response:"+response.status());
        System.out.println("Reaseon:"+response.reason());
        int resCode = response.status()/100;
        return switch (resCode) {
            case 5 -> {
                log.error( TAG, response.status(), response.reason(), exception.getMessage());
                yield new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(),
                        TimeUnit.SECONDS.toMillis(1L), response.request());
            }
            case 4->{
                log.error( TAG, response.status(), response.reason(), exception.getMessage());
                yield new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(),
                        TimeUnit.SECONDS.toMillis(3L), response.request());
            }
            default -> {
                log.error(TAG, response.status(), response.reason(), exception.getMessage());
                yield exception;
            }
        };

    }
}
