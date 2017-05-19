package org.wsipersd.core.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

public class AjaxAwareInvalidSessionStrategy implements InvalidSessionStrategy {
	private final Logger logger = LoggerFactory.getLogger(AjaxAwareInvalidSessionStrategy.class);
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private String destinationUrl;
	private int ajaxResponseErrorCode;
	private boolean createNewSession = true;
	
	
	public AjaxAwareInvalidSessionStrategy() {
	}

	public void onInvalidSessionDetected(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		logger.debug("Starting new session (if required) and redirecting to '" + destinationUrl + "'");
		if (createNewSession) {
			request.getSession();
		}
		if (RequestUtils.isAjaxRequest(request)) {
			response.sendError(ajaxResponseErrorCode, destinationUrl);
		}
		else {
			redirectStrategy.sendRedirect(request, response, destinationUrl);
		}
	}
	
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}

	public void setDestinationUrl(String destinationUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(destinationUrl), "url must start with '/' or with 'http(s)'");
		this.destinationUrl = destinationUrl;
	}

	public void setAjaxResponseErrorCode(int ajaxResponseErrorCode) {
		this.ajaxResponseErrorCode = ajaxResponseErrorCode;
	}

	public int getAjaxResponseErrorCode() {
		return ajaxResponseErrorCode;
	}
}
