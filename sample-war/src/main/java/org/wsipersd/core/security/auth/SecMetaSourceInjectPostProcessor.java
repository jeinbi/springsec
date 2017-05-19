package org.wsipersd.core.security.auth;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class SecMetaSourceInjectPostProcessor implements BeanPostProcessor, InitializingBean{
	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	
	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Object postProcessAfterInitialization(Object bean, String name)
			throws BeansException {
		if (bean instanceof FilterSecurityInterceptor) {
            ((FilterSecurityInterceptor)bean).setSecurityMetadataSource(securityMetadataSource);
        }
        return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

}
