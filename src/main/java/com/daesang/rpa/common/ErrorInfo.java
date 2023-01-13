package com.daesang.rpa.common;

public enum ErrorInfo {

	NO_ONPASSID_INFORMATION("E001", "인증정보 없음"), UNAUTHENTICATED_USER("E002", "인증되지 않은 사용자"),
	AUTHENTICATION_FAILED("E003", "인증처리 실패"), NO_USER_INFORMATION("E004", "등록되지 않은 사용자"),
	EXPIRED_LOGIN_SESSION("E005", "로그인 세션 만료");

	private final String code;
	private final String message;

	ErrorInfo(String code, String message) {

		this.code = code;
		this.message = message;
	}

	public String code() {

		return this.code;
	}

	public String message() {

		return this.message;
	}
}
