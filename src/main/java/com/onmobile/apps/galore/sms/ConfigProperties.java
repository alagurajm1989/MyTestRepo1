package com.onmobile.apps.galore.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
public class ConfigProperties {
	@Bean
	@ConfigurationProperties(prefix = "")
	public Configs config() {
		return new Configs();
	}

	@Data
	public static class Configs {

		private boolean swaggerEnabled;
		private String smsPushMode;
		private String smsPushCountryCode;
		private Sns sns;
	}
	
	@Data
	public static class Sns{
		private String smsSenderid;
		private String smsType;
		private String smsMaxPrice;

	}
	
}
