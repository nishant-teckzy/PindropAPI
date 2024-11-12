package com.novelvox.nbh.pindrop.client.configs;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class PindropFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrDecoder();
    }

    @Bean
    public RequestInterceptor pindropRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager){
        return new PindropRequestInterceptor(authorizedClientManager);
    }
}
