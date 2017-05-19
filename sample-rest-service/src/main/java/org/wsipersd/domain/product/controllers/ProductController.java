package org.wsipersd.domain.product.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {
	
	@RequestMapping(value="/test", method = RequestMethod.GET)
	public  @ResponseBody String showProduct() {
		return "{\"productid\":\"123456\"}";
	}

}
