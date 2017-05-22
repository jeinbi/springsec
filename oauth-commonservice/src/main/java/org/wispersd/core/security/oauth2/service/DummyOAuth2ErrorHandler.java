package org.wispersd.core.security.oauth2.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.http.OAuth2ErrorHandler;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

public class DummyOAuth2ErrorHandler extends OAuth2ErrorHandler
{
	private static final Logger logger = LoggerFactory.getLogger(DummyOAuth2ErrorHandler.class);
	public DummyOAuth2ErrorHandler(final OAuth2ProtectedResourceDetails resource)
	{
		super(resource);
	}
	
	public void handleError(final ClientHttpResponse response) throws IOException {
		logger.error("Error caught while sending oauth request");
	}

}
