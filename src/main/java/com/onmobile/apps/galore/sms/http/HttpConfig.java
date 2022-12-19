package com.onmobile.apps.galore.sms.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "http.config")
public class HttpConfig {
	
    private int connectTimeout;

    private int readTimeout;

    private int poolMax;

    private int poolMaxPerRoute;
    
    private boolean proxyRequired;

    private String proxyHost;
    
    private int proxyPort;
    
    private String proxyUser;
    
    private String proxyPassword;

}