package com.novelvox.nbh.genesys.client.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Collections;
@Log4j2
@Component
public class GenesysRequestInterceptor implements RequestInterceptor {
    private static final String TAG = "OAuthToken >> ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";
    private static final String CLIENT_REGISTRATION_ID = "GenesysSms";

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public GenesysRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    private Authentication createPrincipal() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptySet();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return CLIENT_REGISTRATION_ID;
            }
        };
    }
    @Override
    public void apply(RequestTemplate template) {
        final OAuth2AuthorizeRequest request =
                OAuth2AuthorizeRequest.withClientRegistrationId(CLIENT_REGISTRATION_ID)
                        .principal(createPrincipal())
                        .build();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientManager.authorize(request);//.getAccessToken().getTokenValue();
        log.info(TAG+"{}",oAuth2AuthorizedClient.getAccessToken().getExpiresAt());
        System.out.println("Auth>>>> "+oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        if(!template.headers().containsKey(AUTHORIZATION)){
            template.header(AUTHORIZATION, String.format("%s %s", BEARER_TOKEN_TYPE, oAuth2AuthorizedClient.getAccessToken().getTokenValue()));
        }

    }
}
