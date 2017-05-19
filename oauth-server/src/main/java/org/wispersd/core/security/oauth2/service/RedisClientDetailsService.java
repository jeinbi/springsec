package org.wispersd.core.security.oauth2.service;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class RedisClientDetailsService implements ClientDetailsService, ClientRegistrationService {
	private static final Logger logger = LoggerFactory.getLogger(RedisClientDetailsService.class);
	private RedisTemplate<String, ClientDetails> redisTemplate;
	private HashOperations<String, String, ClientDetails> hashOpsForClientDetails;
	private String tablePrefix = "CLIENTDETAILS";
	
	public RedisTemplate<String, ClientDetails> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, ClientDetails> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void init() {
		hashOpsForClientDetails = redisTemplate.opsForHash();
	}
	
	public void loadFromProperties(String propertyFileName) {
		Properties p = new Properties();
		try {
			p.load(this.getClass().getClassLoader().getResourceAsStream(propertyFileName));
		} catch (IOException e) {
			logger.error("Error loading property " + propertyFileName, e);
		}
		for(Entry<Object,Object> nextEntry:p.entrySet()) {
			String nextKeyStr = (String)nextEntry.getKey();
			String nextValStr = (String)nextEntry.getValue();
			System.out.println(nextValStr);
			ClientDetails clientDetails = (ClientDetails)redisTemplate.getHashValueSerializer().deserialize(nextValStr.getBytes());
			this.addClientDetails(clientDetails);
		}
	}
	

	@Override
	public void addClientDetails(ClientDetails clientDetails)
			throws ClientAlreadyExistsException {
		Boolean res = hashOpsForClientDetails.putIfAbsent(tablePrefix, clientDetails.getClientId(), clientDetails);
		if (!res.booleanValue()) {
			throw new ClientAlreadyExistsException("Client already exists: "+ clientDetails.getClientId());
		}
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails)
			throws NoSuchClientException {
		if (hashOpsForClientDetails.hasKey(tablePrefix, clientDetails.getClientId())) {
			hashOpsForClientDetails.put(tablePrefix, clientDetails.getClientId(), clientDetails);
		}
		else {
			throw new NoSuchClientException("No client exists: " + clientDetails.getClientId());
		}
	}

	@Override
	public void updateClientSecret(String clientId, String secret)
			throws NoSuchClientException {
		ClientDetails clientDetails = loadClientByClientId(clientId);
		if (clientDetails != null) {
			if (clientDetails instanceof BaseClientDetails) {
				((BaseClientDetails)clientDetails).setClientSecret(secret);
				hashOpsForClientDetails.put(tablePrefix, clientId, clientDetails);
			}
		}
		else {
			throw new NoSuchClientException("No client exists: " + clientId);
		}
	}

	@Override
	public void removeClientDetails(String clientId)
			throws NoSuchClientException {
		Long res = hashOpsForClientDetails.delete(tablePrefix, clientId);
		if (res.longValue() == 0) {
			throw new NoSuchClientException("No client exists: " + clientId);
		}
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		return hashOpsForClientDetails.values(tablePrefix);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId)
			throws ClientRegistrationException {
		ClientDetails clientDtls = hashOpsForClientDetails.get(tablePrefix, clientId);
		/*
		Map<String, Object> additionalInfo = clientDtls.getAdditionalInformation();
		if (additionalInfo != null) {
			additionalInfo.get("client");
		}*/
		return clientDtls;
	}
	
	/*
	public static void main(String[] args) {
		ApplicationContext applContext = new ClassPathXmlApplicationContext(new String[]{"oauth2-services.xml", "redis-services.xml"});
		RedisClientDetailsService redisClientDtlService = (RedisClientDetailsService)applContext.getBean("oauthClientDetails");
		redisClientDtlService.loadFromProperties("clientdetails.properties");
	}
	*/
}
