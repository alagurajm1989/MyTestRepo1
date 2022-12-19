package com.onmobile.apps.galore.sms.services.sharechat;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.onmobile.apps.galore.polymer.common.util.JacksonHelper;
import com.onmobile.apps.galore.sms.controller.dto.SmsPushRequest;
import com.onmobile.apps.galore.sms.http.HttpClientConfig;
import com.onmobile.apps.galore.sms.services.sharechat.SharechatOtpModel.KeyValue;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@ConfigurationProperties(prefix = "sharechat")
@Data
public class SharechatSmsService {
    
    private String otpTemplate;

    private String otpEndpoint;

    private String encryptionKey;
    
    @Autowired private HttpClientConfig httpClientConfig;
    
    public boolean sendSms(SmsPushRequest smsPushRequest) {
	log.debug("processing sharechat sms push: {}", smsPushRequest);
	boolean response = false;
	try {
	    SharechatOtpModel model = new SharechatOtpModel();
	    KeyValue keyValue = model.new KeyValue(smsPushRequest.getOtp());
	    model.setKeyValue(keyValue);
	    model.setPhoneNumber(smsPushRequest.getMsisdn());
	    model.setTemplate(otpTemplate);
	    
	    String stringPayload = JacksonHelper.getInstance().getJsonStringFromObject(model);
	    
	    String encodedPayload = signPayload(stringPayload, encryptionKey);
	    SharechatRequest requestBody = new SharechatRequest(encodedPayload);
	    
	    log.debug("calling sharechat endpoint:{} with requestBody = {}", otpEndpoint, requestBody);
	    ResponseEntity<String> responseEntity = httpClientConfig.getRestTemplate().postForEntity(otpEndpoint, requestBody, String.class);
	    
	    log.debug("response from sharechat status={} body= {}", responseEntity.getStatusCode(), responseEntity.getBody());
	    if(responseEntity.getStatusCode() == HttpStatus.OK) {
		response = true;
	    }
	    
	}
	catch (Exception e) {
	    log.error("error in send sharechat otp", e);
	}
	return response;
    }

    private String signPayload(String payload, String encryptionKey) {
	Key signingKey = new SecretKeySpec(encryptionKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
	return Jwts.builder()
		.setPayload(payload)
		.signWith(SignatureAlgorithm.HS256, signingKey )
                .compact();
    }
}
