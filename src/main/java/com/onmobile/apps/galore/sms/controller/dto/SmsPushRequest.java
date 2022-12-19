package com.onmobile.apps.galore.sms.controller.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class SmsPushRequest {
    
    private String msisdn;
    
    @ToString.Exclude
    private String message;
    
    @ToString.Exclude
    private int otp;

    private String messageType;
    private String senderId;
    private String clientId;
    private String countryCode;
    private String operator;
    private String pushMode;
    
    
}
