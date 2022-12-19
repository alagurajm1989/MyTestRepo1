package com.onmobile.apps.galore.sms.services.sharechat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SharechatOtpModel {
    
    private KeyValue keyValue;
    private String template;
    private String phoneNumber;
    
    @Data
    @AllArgsConstructor
    public class KeyValue{
	private int otp;
    }
}
