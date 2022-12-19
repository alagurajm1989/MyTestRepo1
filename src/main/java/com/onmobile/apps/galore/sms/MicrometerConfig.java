package com.onmobile.apps.galore.sms;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;

@Configuration
public class MicrometerConfig {

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
	    return new TimedAspect(registry);
	}

	  @Bean
	  MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
	    return registry -> registry.config()
	        //.commonTags("host", "host","service", "appserver","region", "region")
	        .meterFilter(MeterFilter.deny(id -> {
	          String uri = id.getTag("uri");
	          return uri != null && uri.startsWith("/actuator");
	        }))
	        .meterFilter(MeterFilter.deny(id -> {
		          String uri = id.getTag("uri");
		          return uri != null && uri.startsWith("/public/status");
		        }))
	    	
	    	
	    	.meterFilter(MeterFilter.denyNameStartsWith("system"))
	    	.meterFilter(MeterFilter.denyNameStartsWith("disk"))
//	    	.meterFilter(MeterFilter.denyNameStartsWith("application"))
//	    	.meterFilter(MeterFilter.denyNameStartsWith("tomcat"))
//	    	.meterFilter(MeterFilter.denyNameStartsWith("logback"))
//	        .meterFilter(MeterFilter.denyNameStartsWith("hikaricp"))
//	        .meterFilter(MeterFilter.denyNameStartsWith("jvm"))
	        .meterFilter(MeterFilter.denyNameStartsWith("cache"))
//	    	.meterFilter(MeterFilter.denyNameStartsWith("executor"))
//	    	.meterFilter(MeterFilter.denyNameStartsWith("spring"))
//	    	.meterFilter(MeterFilter.denyNameStartsWith("jdbc"))
//	    	
	    	;
	  }
	  
}
