package org.wispersd.core.security.oauth2.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices{
	private RedisTemplate<String, OAuth2Authentication> redisTemplate;
	
	
	
	public RedisTemplate<String, OAuth2Authentication> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(
			RedisTemplate<String, OAuth2Authentication> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
