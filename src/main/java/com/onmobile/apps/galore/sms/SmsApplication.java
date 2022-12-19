package com.onmobile.apps.galore.sms;

import java.io.File;
import java.util.Properties;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import com.onmobile.apps.galore.polymer.util.CAConstants;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
								  HibernateJpaAutoConfiguration.class,
								  RedisAutoConfiguration.class
								  })
@ComponentScan(basePackages = {"com.onmobile.apps.galore.sms",
				"com.onmobile.apps.galore.polymer.aws",
				"com.onmobile.apps.galore.polymer.twilio"
				})
public class SmsApplication {

    private static final Logger logger = LoggerFactory.getLogger(SmsApplication.class);
    
    
    @PostConstruct
    private void init(){
      // Setting Spring Boot SetTimeZone
      TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    
    public static void main(String[] args) {
    	logger.info("Starting SMS Server...");
	new SpringApplicationBuilder().sources(SmsApplication.class).properties(getProperties()).run(args);
	System.out.println("SMS Server is running");
	logger.info("SMS Server is up !!!");
	logger.info("SMS has started with args :");
	for (int i = 0; i < args.length; i++) {
	    logger.info("    " + args[i]);
	}
	
    }
    
    private static Properties getProperties() {
	String propertyFile = System.getenv(CAConstants.CA_HOME)+ File.separator + "smsserver.properties";
	
	System.out.println(propertyFile+"...................................................");
	logger.info("Loading Properties file as: {}", propertyFile);
	
	Properties props = new Properties();
	props.put("spring.config.additional-location", propertyFile);
	props.put("spring.cloud.bootstrap.location", "classpath:bootstrap.properties");
	
	return props;
    }
}
