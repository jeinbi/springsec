package org.wsipersd.core.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AjaxAwareUrlAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private int ajaxResponseErrorCode;
	private String myDefaultFailureUrl = null;
	
	public void setAjaxResponseErrorCode(int ajaxResponseErrorCode) {
		this.ajaxResponseErrorCode = ajaxResponseErrorCode;
	}

	public AjaxAwareUrlAuthFailureHandler() {
		super();
	}

	public AjaxAwareUrlAuthFailureHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
		this.myDefaultFailureUrl = defaultFailureUrl;

	}

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (RequestUtils.isAjaxRequest(request)) {
			response.sendError(ajaxResponseErrorCode, myDefaultFailureUrl);
		}
		else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}

}
