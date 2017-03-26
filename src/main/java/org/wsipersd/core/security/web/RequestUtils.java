package org.wsipersd.core.security.web;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
	private static final String AJAX_REQ_HEADERNAME = "X-Requested-With";
	private static final String AJAX_REQ_HEADERVALUE = "XMLHttpRequest";

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String ajaxHeader = ((HttpServletRequest) request).getHeader(AJAX_REQ_HEADERNAME);
		if (AJAX_REQ_HEADERVALUE.equals(ajaxHeader)) {
			return true;
		}
		return false;
	}

}
