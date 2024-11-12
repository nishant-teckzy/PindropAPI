package com.novelvox.nbh.genesys.client.configs;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
public class FeignErrDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    private static final String TAG = "Genesys API Error >> Status:{},Reason:{},Message:{}";
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = FeignException.errorStatus(methodKey, response);
        log.error(TAG, response.status(), response.reason(), exception.getMessage());
        return exception;

    }
}
