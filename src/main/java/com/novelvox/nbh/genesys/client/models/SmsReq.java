package com.novelvox.nbh.genesys.client.models;

import lombok.Data;

@Data
public class SmsReq {
    private final String fromAddress;
    private final String toAddress;
    private final String toAddressMessengerType;
    private final String textBody;
    private String messagingTemplate;
    private Boolean useExistingActiveConversation;
}
