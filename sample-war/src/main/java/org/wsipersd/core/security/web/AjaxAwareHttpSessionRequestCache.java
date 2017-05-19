package org.wsipersd.core.security.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

public class AjaxAwareHttpSessionRequestCache extends HttpSessionRequestCache {
	private static final Logger logger = LoggerFactory.getLogger(AjaxAwareHttpSessionRequestCache.class);
	
	@Override
	public void saveRequest(HttpServletRequest request,
			HttpServletResponse response) {
		if (RequestUtils.isAjaxRequest(request)) {
			logger.debug("Skip saving request");
		}
		else {
			super.saveRequest(request, response);
		}
	}
	

}
