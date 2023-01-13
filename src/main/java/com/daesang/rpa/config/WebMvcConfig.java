package com.daesang.rpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.daesang.rpa.aspect.AuthenticationInterceptor;
import com.daesang.rpa.aspect.IPCheckInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	final private AuthenticationInterceptor authenticationInterceptor;
	final private IPCheckInterceptor iPCheckInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// Static 컨텐츠와 Proxy API, SSO를 제외한 모든 동작 요청은 인증 대상
		registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**").excludePathPatterns("/css/**")
				.excludePathPatterns("/fonts/**").excludePathPatterns("/images/**").excludePathPatterns("/js/**")
				.excludePathPatterns("/favicon.ico").excludePathPatterns("/ext/**").excludePathPatterns("/proxy/**");

		// Proxy API 호출 시, 사전 허용된 대상만 호출 가능
		registry.addInterceptor(iPCheckInterceptor).addPathPatterns("/proxy/**");
	}
}
