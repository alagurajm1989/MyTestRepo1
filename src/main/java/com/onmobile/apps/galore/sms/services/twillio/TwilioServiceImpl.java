package com.onmobile.apps.galore.sms.services.twillio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class TwilioServiceImpl implements TwilioService{

    String TWILIO_NUMBER;
    
    @Autowired
    public TwilioServiceImpl(String accountSid, String authToken, String twilioNumber) throws Exception{
	Twilio.init(accountSid, authToken);
	this.TWILIO_NUMBER = twilioNumber;
    }

    @Override
    public boolean pustTextMessage(String msisdnWithCountryCode, String textMessage) throws Exception {
	log.debug("sending sms to msisdn using TWILIO ::{}", msisdnWithCountryCode);
	Message message = Message.creator(new PhoneNumber(msisdnWithCountryCode),
		    new PhoneNumber(TWILIO_NUMBER), 
		    textMessage)
		.create();
	log.debug("message send to msisdn::{}, status::{}", msisdnWithCountryCode , message.getStatus());
	if(message.getStatus()!=null) {
	    return true;
	}
	return false;
    }

}
