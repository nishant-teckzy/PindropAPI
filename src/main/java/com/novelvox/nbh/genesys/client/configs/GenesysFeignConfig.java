package com.novelvox.nbh.genesys.client.configs;

import com.novelvox.nbh.pindrop.client.configs.FeignErrDecoder;
import com.novelvox.nbh.pindrop.client.configs.PindropRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

public class GenesysFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrDecoder();
    }

    @Bean
    public RequestInterceptor genesysRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager){
        return new GenesysRequestInterceptor(authorizedClientManager);
    }
}
