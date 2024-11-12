package com.novelvox.nbh.genesys.client.models;

import lombok.Data;

@Data
public class SmsRes {
    public String id;
    public String conversationId;
    public String fromAddress;
    public String toAddress;
    public String messengerType;
    public String textBody;
    public Boolean useExistingActiveConversation;
    public String timestamp;
    public String selfUri;
}
