package com.daesang.rpa.aspect;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.daesang.rpa.common.ErrorInfo;
import com.daesang.rpa.service.CommonService;
import com.nhncorp.lucy.security.xss.XssPreventer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

	@Value("${rpa.ext.url}")
	private String EXT_URL;

	@Value("${rpa.sso.ignore}")
	private Boolean SSO_IGNORE;

	@Value("${rpa.sso.url}")
	private String SSO_URL;

	@Value("${rpa.base.user}")
	private String BASE_USER;

	final private CommonService commonService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {

		if (SSO_IGNORE == true) {

			// TODO 테스트를 위한 임시코드
			String tenant = commonService.getValidTenantId(request.getParameter("tenantId"));

			if (StringUtils.hasText(tenant)) {

				log.warn(XssPreventer
						.escape("*** 테스트를 위하여 SSO 모듈을 사용하지 않고 " + tenant + " 테넌트의 " + BASE_USER + " 계정을 사용하여 로그인"));
			}

			// 세션 생성 후 기본 사용자 정보 등록
			HttpSession session = request.getSession(true);
			session.setAttribute("userInfo", commonService.retrieveTenantMember(tenant, BASE_USER));

			return true;
		}

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userInfo") == null) {

			String message = ErrorInfo.EXPIRED_LOGIN_SESSION.message();

			String characterEncoding = request.getCharacterEncoding();
			if (characterEncoding == null) {
				characterEncoding = "UTF-8";
			}

			response.setCharacterEncoding(characterEncoding);

			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("json") >= 0) {

				response.setStatus(500);
				response.setContentType(contentType);
				response.getWriter()
						.write("{\"message\":\"시스템을 이용할 수 없습니다. [" + message + "]\", \"ssoUrl\":\"" + SSO_URL + "\"}");

			} else {

				response.setContentType("text/html");
				response.getWriter().write("<script>");
				response.getWriter().write("  alert('시스템을 이용할 수 없습니다. [" + message + "]');");
				response.getWriter().write("  window.location.href=\"" + SSO_URL + "\";");
				response.getWriter().write("</script>");
			}

			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {

		// 페이지 요청의 경우, 사용자정보를 등록
		if (modelAndView != null) {

			modelAndView.addObject("ssoUrl", SSO_URL);
			modelAndView.addObject("extUrl", EXT_URL);
			modelAndView.addObject("userInfo", commonService.getUserInfo(request));
		}
	}
}
