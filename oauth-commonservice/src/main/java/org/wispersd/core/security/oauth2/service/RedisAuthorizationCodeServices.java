package org.wispersd.core.security.oauth2.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices{
	private RedisTemplate<String, OAuth2Authentication> redisTemplate;
	private HashOperations<String, String, OAuth2Authentication> hashOpsForAuthCode;
	private final String tablePrefix = "AUTHCODE";



	public RedisTemplate<String, OAuth2Authentication> getRedisTemplate()
	{
		return redisTemplate;
	}

	public void setRedisTemplate(final RedisTemplate<String, OAuth2Authentication> redisTemplate)
	{
		this.redisTemplate = redisTemplate;
	}

	public void init()
	{
		hashOpsForAuthCode = redisTemplate.opsForHash();
	}

	@Override
	protected void store(final String code, final OAuth2Authentication authentication)
	{
		hashOpsForAuthCode.put(tablePrefix, code, authentication);
	}

	@Override
	protected OAuth2Authentication remove(final String code)
	{
		final OAuth2Authentication auth = hashOpsForAuthCode.get(tablePrefix, code);
		hashOpsForAuthCode.delete(tablePrefix, code);
		return auth;
	}

}
