package com.novelvox.nbh.genesys.client.configs;

import com.novelvox.nbh.genesys.client.models.Scripts;
import com.novelvox.nbh.genesys.client.models.SmsReq;
import com.novelvox.nbh.genesys.client.models.SmsRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="GenesysSms", configuration=GenesysFeignConfig.class)
public interface GenesysClient {

    @GetMapping("scripts")
    public Scripts getScripts();

    @PostMapping(value="conversations/messages/agentless", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SmsRes sendSms(SmsReq smsReq);
}
