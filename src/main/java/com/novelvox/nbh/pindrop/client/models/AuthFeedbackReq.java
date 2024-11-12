package com.novelvox.nbh.pindrop.client.models;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthFeedbackReq {
    private String interaction_id;
    private String account_alias;
    private String external_verification;
}
