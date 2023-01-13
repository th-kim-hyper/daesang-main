package com.daesang.rpa.aspect;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.daesang.rpa.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class IPCheckInterceptor implements HandlerInterceptor {

	@Value("${rpa.proxy.allowed}")
	private String PROXY_ALLOWED;

	@Value("${rpa.sso.ignore}")
	private Boolean SSO_IGNORE;

	final private CommonService commonService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		if (SSO_IGNORE != true) {

			String reqIp = commonService.getRemoteAddr(request);
			log.warn("*** 요청된 Proxy API 호출 IP : " + reqIp);

			String[] allowed = PROXY_ALLOWED.split(",");
			for (String ip : allowed) {

				if (reqIp.indexOf(ip) >= 0) {

					log.warn("*** 인가된 Proxy API 호출 IP : " + ip);

					return true;
				}
			}

			log.warn("*** 인가되지 않은 Proxy API 호출 : " + reqIp);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().write("{\"message\":\"시스템을 이용할 수 없습니다. [인가되지 않은 IP]\"}");

			return false;
		}

		return true;
	}
}
