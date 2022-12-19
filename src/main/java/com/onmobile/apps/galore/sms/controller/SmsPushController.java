package com.onmobile.apps.galore.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.onmobile.apps.galore.sms.controller.dto.SmsPushRequest;
import com.onmobile.apps.galore.sms.services.SmsPushService;
import com.onmobile.apps.galore.sms.services.sharechat.SharechatRequest;

@RestController
@RequestMapping("/")
public class SmsPushController {
    
    private static final Logger log = LoggerFactory.getLogger(SmsPushController.class);
    
    
    @Autowired 
    SmsPushService smsPushService;
    
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public ResponseEntity<String> pushMessage( @ModelAttribute("smsPushRequest") SmsPushRequest smsPushRequest) throws Exception {
	log.debug("received sms push request::{}", smsPushRequest);
	String response = smsPushService.sendSms(smsPushRequest);
	return new ResponseEntity<String>(response,HttpStatus.OK);
    }
    
    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMessage( @RequestBody SmsPushRequest smsPushRequest) throws Exception {
	log.debug("received sms push request::{}", smsPushRequest);
	String response = smsPushService.sendSms(smsPushRequest);
	return new ResponseEntity<String>(response,HttpStatus.OK);
    }

}
