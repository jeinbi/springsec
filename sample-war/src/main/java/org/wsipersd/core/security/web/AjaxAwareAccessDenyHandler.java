package org.wsipersd.core.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

public class AjaxAwareAccessDenyHandler extends AccessDeniedHandlerImpl {
	private String myErrorPage;
	private int ajaxResponseErrorCode;
	
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		if (!response.isCommitted()) {
			if (RequestUtils.isAjaxRequest(request)) {
				if (myErrorPage != null) {
					response.sendError(ajaxResponseErrorCode, myErrorPage);
				}
				else {
					response.sendError(ajaxResponseErrorCode);
				}
			}
			else {
				super.handle(request, response, accessDeniedException);
			}
		}
	}
	
	public void setErrorPage(String errorPage) {
		super.setErrorPage(errorPage);
		this.myErrorPage = errorPage;
	}
	

	public int getAjaxResponseErrorCode() {
		return ajaxResponseErrorCode;
	}

	public void setAjaxResponseErrorCode(int ajaxResponseErrorCode) {
		this.ajaxResponseErrorCode = ajaxResponseErrorCode;
	}
}
