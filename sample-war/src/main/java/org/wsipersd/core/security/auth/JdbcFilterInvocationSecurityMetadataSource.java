package org.wsipersd.core.security.auth;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class JdbcFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        HttpServletRequest request = fi.getHttpRequest();
        
        String[] roles = new String[] { "ROLE_ADMIN", "ROLE_USER" };
        return SecurityConfig.createList(roles);
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return Collections.emptyList();
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
