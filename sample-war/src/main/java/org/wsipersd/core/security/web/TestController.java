package org.wsipersd.core.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	@RequestMapping(value="/test", method = RequestMethod.GET)
	public @ResponseBody String performTest() {
		return "{\"result\":\"success\"}";
	}
}
