package com.novelvox.nbh.genesys.client.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Otp {
    private String otpCode;
    private LocalDateTime expirationTime;
}
