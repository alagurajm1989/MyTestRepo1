package com.onmobile.apps.galore.sms.services.twillio;

public interface TwilioService {

    public boolean pustTextMessage(String msisdnWithCountryCode, String message) throws Exception;

}
