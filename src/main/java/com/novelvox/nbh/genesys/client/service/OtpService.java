package com.novelvox.nbh.genesys.client.service;

import com.novelvox.nbh.genesys.client.configs.GenesysClient;
import com.novelvox.nbh.genesys.client.models.Otp;
import com.novelvox.nbh.genesys.client.models.Scripts;
import com.novelvox.nbh.genesys.client.models.SmsReq;
import com.novelvox.nbh.genesys.client.models.SmsRes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class OtpService {

    private final int expirationMin;
    private final int otpLength;
    private final String fromAddress;
    private final String toAddressMessengerType;
    private final String preText;
    private final Random random = new Random();
    private final GenesysClient genesysClient;
    private final Map<String, Otp> otpStorage = new ConcurrentHashMap<>();

    public OtpService(@Value("${genesys.otp.expiration_min}") int expirationMinutes,
                      @Value("${genesys.otp.length}") int otpLength,
                      @Value("${genesys.otp.fromAddress}") String fromAddress,
                      @Value("${genesys.otp.toAddressMessengerType}") String toAddressMessengerType,
                      @Value("${genesys.otp.pre_text}") String preText,
                      GenesysClient genesysClient) {
        this.expirationMin = expirationMinutes;
        this.otpLength = otpLength;
        this.fromAddress = fromAddress;
        this.toAddressMessengerType = toAddressMessengerType;
        this.preText = preText;
        this.genesysClient = genesysClient;

        log.info("OtpService initialized with expirationMinutes: {}, otpLength: {}, fromAddress: {}, toAddressMessengerType: {}",
                expirationMinutes, otpLength, fromAddress, toAddressMessengerType);
    }

    public String generateOtp(String phoneNumber) {
        log.info("Generating OTP for phone number: {}", phoneNumber);
        try{
        String otpCode = String.format("%0" + otpLength + "d", random.nextInt(999999));
        Otp otp = new Otp(otpCode, LocalDateTime.now().plusMinutes(expirationMin));
        otpStorage.put(phoneNumber, otp);

        log.debug("OTP: {} generated for phone number: {}, expires at: {}", otpCode, phoneNumber, otp.getExpirationTime());


            sendOtp(phoneNumber, otpCode);
            return otpCode;
        }catch(Exception e){
            log.error("Error generating OTP: {}", e.getMessage());
            throw e;
        }
    }

    private SmsRes sendOtp(String phoneNumber, String otpCode) {
        try {
            SmsReq smsReq = new SmsReq(fromAddress, phoneNumber, toAddressMessengerType, preText + otpCode);
            log.info("Sending OTP: {} to phone number: {}", otpCode, phoneNumber);
            SmsRes otpRes = genesysClient.sendSms(smsReq);
            log.debug("OTP sent successfully to {}", otpRes);
            return otpRes;
        } catch (Exception e) {
            log.error("Failed to send OTP to phone number: {}. Error: {}", phoneNumber, e.getMessage(), e);
            throw e;
        }

    }

    public String getOtp(String phoneNumber) {
        log.info("Retrieving OTP for phone number: {}", phoneNumber);

        Otp otp = otpStorage.get(phoneNumber);
        if (otp != null) {
            if (otp.getExpirationTime().isAfter(LocalDateTime.now())) {
                log.debug("OTP: {} is valid for phone number: {}", otp.getOtpCode(), phoneNumber);
                return otp.getOtpCode();
            } else {
                log.warn("OTP for phone number: {} has expired", phoneNumber);
            }
        } else {
            log.warn("No OTP found for phone number: {}", phoneNumber);
        }
        return null;
    }

    @Scheduled(fixedRate = 60000)
    public void removeExpiredOtps() {
        log.info("Running scheduled task to remove expired OTPs");

        long initialSize = otpStorage.size();
        otpStorage.entrySet().removeIf(entry -> entry.getValue().getExpirationTime().isBefore(LocalDateTime.now()));
        long finalSize = otpStorage.size();

        log.info("Expired OTPs removed. Initial size: {}, Final size: {}", initialSize, finalSize);
    }

    public Scripts getScripts() {
        log.info("Fetching scripts from GenesysClient");
        Scripts scripts = genesysClient.getScripts();
        log.debug("Fetched scripts: {}", scripts);
        return scripts;
    }
}
