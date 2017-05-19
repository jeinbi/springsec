package org.wsipersd.core.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

public class AjaxAwareAuthenticationSuccessHandler extends
	SavedRequestAwareAuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();

	public AjaxAwareAuthenticationSuccessHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			handleAjaxAwareDefaultSuccess(request, response, authentication);

			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			handleAjaxAwareDefaultSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);

		// Use the DefaultSavedRequest URL
		String targetUrl = savedRequest.getRedirectUrl();
		this.handleAjaxAwareSavedReqSuccess(request, response, authentication, targetUrl);
	}
	
	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}
	
	protected void handleAjaxAwareDefaultSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		if (RequestUtils.isAjaxRequest(request)) {
			String targetUrl = determineTargetUrl(request,response); 
			prepareAjaxSuccessResp(response, targetUrl);
		}
		else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
	
	protected void handleAjaxAwareSavedReqSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication, String targetUrl) throws ServletException, IOException {
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		if (RequestUtils.isAjaxRequest(request)) {
			prepareAjaxSuccessResp(response, targetUrl);
		}
		else {
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
	}
	
	protected void prepareAjaxSuccessResp(HttpServletResponse response, String targetUrl) throws ServletException, IOException {
		String jsonObj = "{\"success\":\"true\",\"targeturl\":\""+ targetUrl + "\"}";
		response.setContentType("application/json");
		response.getWriter().println(jsonObj);
	}
}
