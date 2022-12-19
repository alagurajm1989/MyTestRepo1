package com.onmobile.apps.galore.sms.services.impl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.onmobile.apps.galore.polymer.aws.service.AmazonSNSClientService;
import com.onmobile.apps.galore.polymer.common.exceptions.CustomException;
import com.onmobile.apps.galore.polymer.common.exceptions.ResponseCodes;
import com.onmobile.apps.galore.polymer.common.exceptions.RestErrorResponse;
import com.onmobile.apps.galore.sms.ConfigProperties.Configs;
import com.onmobile.apps.galore.sms.controller.dto.SmsPushRequest;
import com.onmobile.apps.galore.sms.enums.PushMode;
import com.onmobile.apps.galore.sms.services.SmsPushService;
import com.onmobile.apps.galore.sms.services.sharechat.SharechatSmsService;
import com.onmobile.apps.galore.sms.services.twillio.TwilioService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class SmsPushServiceImpl implements SmsPushService{
    
    @Autowired
    AmazonSNSClientService amazonSNSClient;
    
    @Autowired
    TwilioService twilioService;
    
    @Autowired
    private Configs config;

    @Autowired private SharechatSmsService sharechatSmsService;

    @Override
    public String sendSms(SmsPushRequest smsPushRequest) throws Exception {
	
	String pushMode = smsPushRequest.getPushMode();
	if(pushMode==null || pushMode.length()==0) {
	    pushMode = config.getSmsPushMode();
	}
	pushMode = pushMode.toUpperCase();
	boolean smsPushStatus = false;
	
	PushMode smsPushMode = PushMode.valueOf(pushMode);
	log.debug("using sms push mode ::{}", pushMode);
	switch (smsPushMode) {
	case AWSSNS:
	    smsPushStatus = awsSnsSmsPush(smsPushRequest);
	    break;

	case TWILIO:
	    smsPushStatus = twilioSmsPush(smsPushRequest);
	    break;
	case UMP:
	    smsPushStatus = umpSmsPush(smsPushRequest);
	    break;
	case SHARECHAT:
	    smsPushStatus = sharechatSmsService.sendSms(smsPushRequest);
	    break;
	default:
	    break;
	}

	if(smsPushStatus) {
	    return ResponseCodes.SUCCESS.name();
	}
	else throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, RestErrorResponse.build(ResponseCodes.SMS_SEND_FAILED));
    }


    private boolean umpSmsPush(SmsPushRequest smsPushRequest) {
	// TODO Auto-generated method stub
	return false;
    }


    private boolean twilioSmsPush(SmsPushRequest smsPushRequest) throws Exception {
	String countryCode = (smsPushRequest.getCountryCode() == null || smsPushRequest.getCountryCode().length()==0) ? config.getSmsPushCountryCode() : smsPushRequest.getCountryCode();
	if(!countryCode.startsWith("+")) {
	    countryCode = "+"+countryCode;
	}
	log.debug("using countryCode::{}", countryCode);
	String msisdnWithCountryCode = countryCode + smsPushRequest.getMsisdn() ;
	
//	String senderId = smsPushRequest.getSenderId();
//	if(senderId == null || senderId.length()==0) {
//	    senderId = defaultSenderId;
//	}

	String message = URLDecoder.decode(smsPushRequest.getMessage(),"UTF-8");
	return twilioService.pustTextMessage(msisdnWithCountryCode, message);
    }


    private boolean awsSnsSmsPush(SmsPushRequest smsPushRequest) throws Exception {
	String countryCode = (smsPushRequest.getCountryCode() == null || smsPushRequest.getCountryCode().length()==0) ? config.getSmsPushCountryCode() : smsPushRequest.getCountryCode();
	log.debug("using countryCode::{}", countryCode);
	
	String msisdnWithCountryCode = countryCode + smsPushRequest.getMsisdn() ;
	
	
	Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();

	String senderId = smsPushRequest.getSenderId();
	if(senderId == null || senderId.length()==0) {
	    senderId = config.getSns().getSmsSenderid();
	}
	smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue().withStringValue(senderId).withDataType("String"));
	if(config.getSns().getSmsMaxPrice() !=null) {
	    smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue().withStringValue(config.getSns().getSmsMaxPrice()).withDataType("Number"));
	}
	if(config.getSns().getSmsType()!=null) {
	    smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue(config.getSns().getSmsType()).withDataType("String"));
	}
	else {
	    smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
	}

	String message = URLDecoder.decode(smsPushRequest.getMessage(),"UTF-8");
	return amazonSNSClient.sendTextMessage(msisdnWithCountryCode, message, smsAttributes);
    }

   
}
