package com.onmobile.apps.galore.sms;


import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.onmobile.apps.galore.sms.ConfigProperties.Configs;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurationSupport {
	
	@Autowired
	private Configs config;
    
    @Bean
    public Docket allApi() {
        return new Docket(DocumentationType.SWAGGER_2).enable(config.isSwaggerEnabled())
        					      .groupName("all-apis")
        					      .select()
                                                      .apis(RequestHandlerSelectors.basePackage("com.onmobile.apps.galore"))
                                                      .paths(PathSelectors.any())
                                                      .build()
                                                      .apiInfo(info());
    }


    private ApiInfo info() {
        return new ApiInfoBuilder().title("SMS Server")
                                   .description("SMSServer for contest PWA client ")
                                   .contact(new Contact("Manav Shah", "https://www.onmobile.com","manav.shah@onmobile.com"))
                                   .version("1.0.0")
                                   .build();
    }

    
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	configurer.enable();
    }
    
 
    private List<SecurityReference> defaultAuth() {
	final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
	final AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { authorizationScope };
	return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

 
}