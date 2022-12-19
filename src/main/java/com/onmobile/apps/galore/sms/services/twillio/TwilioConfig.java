package com.onmobile.apps.galore.sms.services.twillio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
public class TwilioConfig {

    
    public static String ACCOUNT_SID = "AC28ed4c9b63d5df2575939d85adf1062e";
    public static String AUTH_TOKEN = "bd50bdef19032a8f0608b57c7692fbe9";
    public static String TWILIO_NUMBER = "+12512377761";

    private String accountSid;
    private String authToken;
    private String number;
    
    @Bean(name="twilioNumber")
    public String getTwilioNumber() {
	return number;
    }

    @Bean(name="accountSid")
    public String getAccountSid() {
	return accountSid;
    }

    @Bean(name="authToken")
    public String getAuthToken() {
	return authToken;
    }

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
    
    

}
