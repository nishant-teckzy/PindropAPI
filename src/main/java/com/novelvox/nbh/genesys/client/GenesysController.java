package com.novelvox.nbh.genesys.client;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novelvox.nbh.commons.Util;
import com.novelvox.nbh.genesys.client.service.OtpService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/genesys")
public class GenesysController {
    private static final String TAG = "Genesys_Controller";
    @Autowired
    private OtpService otpService;

    @GetMapping(path = "/test")
    public ResponseEntity<String> test() {
        log.info("{}: Invoked /genesys/test endpoint", TAG);
        String scripts = otpService.getScripts().toString();
        log.debug("{}: Fetched scripts: {}", TAG, scripts);
        return ResponseEntity.ok(scripts);
    }

    @GetMapping("/")
    public List<String> index() throws IOException {
        log.info("{}: Invoked /genesys endpoint", TAG);

        List<String> questions = Util.getRandomQuestions(3);
        log.debug("{}: Fetched questions: {}", TAG, questions);
        return questions;
    }

    @GetMapping("/agentless/sms/{toAddress}")
    public ResponseEntity<String> sendSms(@PathVariable String toAddress) {
        log.info("{}: Invoked /genesys/agentless/sms/{} endpoint", TAG, toAddress);

        if (toAddress == null || toAddress.isBlank()) {
            log.error("{}: Invalid toAddress provided: {}", TAG, toAddress);
            return ResponseEntity.badRequest().build();
        }

        log.debug("{}: Generating OTP for address: {}", TAG, toAddress);
        String otp = otpService.generateOtp(toAddress);

        if (otp != null && !otp.isBlank()) {
            log.info("{}: OTP generated successfully for {}: {}", TAG, toAddress, otp);
            return ResponseEntity.ok(otp);
        } else {
            log.error("{}: OTP generation failed for address: {}", TAG, toAddress);
            return ResponseEntity.noContent().build();
        }
    }
}
