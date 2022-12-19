package com.onmobile.apps.galore.sms.http;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class HttpClientConfig {

    @Autowired HttpConfig httpConfig;
    private RestTemplate restTemplate;
    private RestTemplate withoutProxyRestTemplate;
    
    @PostConstruct
    private void init() throws Exception {

	log.info("Initializing http-client connection pools!!!");
	RestTemplate restTemplate = getRestTemplate(httpConfig);
	if (!httpConfig.isProxyRequired()) {
	    this.withoutProxyRestTemplate = restTemplate;
	} else {
	    this.restTemplate = restTemplate;
	}
	log.info("Http client connection pool created ");
    }
    
    private RestTemplate getRestTemplate(HttpConfig httpConfig) {
	RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory(httpConfig));
	
	List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

	messageConverters.add(new FormHttpMessageConverter());

	messageConverters.add(new StringHttpMessageConverter());

	messageConverters.add(new ByteArrayHttpMessageConverter());

/*	MarshallingHttpMessageConverter marshallinghttpmessageconverter = new MarshallingHttpMessageConverter();
	messageConverters.add(marshallinghttpmessageconverter);*/

	messageConverters.add(new SourceHttpMessageConverter());

	MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
	ObjectMapper newObjectMapper = getObjectMapper(false);
	jsonConverter.setObjectMapper(newObjectMapper);
	List<MediaType> jsonConvertermediaTypes = new ArrayList<MediaType>();
	jsonConvertermediaTypes.add(MediaType.APPLICATION_JSON);
	jsonConvertermediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
	jsonConverter.setSupportedMediaTypes(jsonConvertermediaTypes);
	messageConverters.add(jsonConverter);
	
	//messageConverters.add(new MappingJackson2HttpMessageConverter());

	Jaxb2RootElementHttpMessageConverter jaxbMessageConverter = new Jaxb2RootElementHttpMessageConverter();
	List<MediaType> mediaTypes = new ArrayList<MediaType>();
	mediaTypes.add(MediaType.TEXT_HTML);
	mediaTypes.add(MediaType.APPLICATION_XML);
	mediaTypes.add(MediaType.TEXT_XML);
	jaxbMessageConverter.setSupportedMediaTypes(mediaTypes);
	messageConverters.add(jaxbMessageConverter);

	restTemplate.setMessageConverters(messageConverters);

	return restTemplate;
    }
    
    private ClientHttpRequestFactory getClientHttpRequestFactory(HttpConfig httpConfig) {
	HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(getHttpClient(httpConfig));
	factory.setConnectTimeout(httpConfig.getConnectTimeout());
	factory.setReadTimeout(httpConfig.getReadTimeout());

	return factory;
    }
    
    private HttpClient getHttpClient(HttpConfig httpConfig) {
	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	cm.setMaxTotal(httpConfig.getPoolMax());
	cm.setDefaultMaxPerRoute(httpConfig.getPoolMaxPerRoute());
	
	if(httpConfig.isProxyRequired()){
	    return HttpClients
		.custom()
			.setProxy(new HttpHost(httpConfig.getProxyHost(), httpConfig.getProxyPort()))
			.setConnectionManager(cm).build();
	}else{
	    return HttpClients
		.custom().setConnectionManager(cm).build();
	}

    }

    public RestTemplate getRestTemplate(){
	if(this.restTemplate != null) {
	    return this.restTemplate;
	}
	return this.getRestTemplateWithoutProxy();
    }
    


    public RestTemplate getRestTemplateWithoutProxy(){
	return this.withoutProxyRestTemplate;
    }

    public static ObjectMapper getObjectMapper(boolean failOnUnknowProperties) {
	ObjectMapper objectMapper = new ObjectMapper();
	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknowProperties);
	objectMapper.setSerializationInclusion(Include.NON_NULL);
	return objectMapper;
    }
    
}
