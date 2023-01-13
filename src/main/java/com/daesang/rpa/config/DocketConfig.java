package com.daesang.rpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@RequiredArgsConstructor
@Configuration
public class DocketConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30).useDefaultResponseMessages(false).select()
				.apis(RequestHandlerSelectors.basePackage("com.daesang.rpa.controller")).paths(PathSelectors.any())
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("DAESANG RPA PORTAL").description("API Information").version("1.0").build();
	}
}
