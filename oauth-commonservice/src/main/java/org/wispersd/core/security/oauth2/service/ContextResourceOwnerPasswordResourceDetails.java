package org.wispersd.core.security.oauth2.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

public class ContextResourceOwnerPasswordResourceDetails extends ResourceOwnerPasswordResourceDetails
{
	@Override
	public String getUsername()
	{
		Authentication auth = getAuthentication();
		return auth.getName();
	}

	@Override
	public String getPassword()
	{
		Authentication auth = getAuthentication();
		return auth.getCredentials().toString();
	}
	
	private Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		return auth;
	}

}
