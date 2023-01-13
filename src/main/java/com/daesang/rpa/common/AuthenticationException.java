package com.daesang.rpa.common;

public class AuthenticationException extends Exception {

	private static final long serialVersionUID = -325576253699554772L;

	private ErrorInfo errorInfo;

	public AuthenticationException(String message) {

		super(message);
	}

	public AuthenticationException(ErrorInfo errorInfo) {

		super(errorInfo.message());

		this.errorInfo = errorInfo;
	}

	public ErrorInfo getErrorInfo() {

		return this.errorInfo;
	}
}