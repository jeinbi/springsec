package org.wsipersd.core.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{
	private int ajaxResponseErrorCode;
	
	

	public int getAjaxResponseErrorCode() {
		return ajaxResponseErrorCode;
	}

	public void setAjaxResponseErrorCode(int ajaxResponseErrorCode) {
		this.ajaxResponseErrorCode = ajaxResponseErrorCode;
	}

	public AjaxAwareAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
		    throws IOException, ServletException {
		if (RequestUtils.isAjaxRequest(request)) {
			response.sendError(ajaxResponseErrorCode);
		}
		else {
			super.commence(request, response, authException);
		}
	}

}
