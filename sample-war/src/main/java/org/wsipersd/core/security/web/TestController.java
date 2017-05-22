package org.wsipersd.core.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	private OAuth2RestTemplate oAuth2RestTemplate;
	
	public OAuth2RestTemplate getoAuth2RestTemplate() {
		return oAuth2RestTemplate;
	}


	@Autowired
	public void setoAuth2RestTemplate(OAuth2RestTemplate oAuth2RestTemplate) {
		this.oAuth2RestTemplate = oAuth2RestTemplate;
	}

	@RequestMapping(value="/test", method = RequestMethod.GET)
	public @ResponseBody String performTest() {
		ResponseEntity<String> respEntity = oAuth2RestTemplate.getForEntity("http://localhost:8080/restservice/test", String.class);
		return respEntity.getBody();
	}
}
