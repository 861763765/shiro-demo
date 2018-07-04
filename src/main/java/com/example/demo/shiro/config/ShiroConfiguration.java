package com.example.demo.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * shrio配置
 * </p>
 *
 * @author sam.king
 * @date 2018年5月8日
 */
@Configuration
@Slf4j
public class ShiroConfiguration {

	/**
	 * <p>
	 * 自定义验证方式
	 * </p>
	 *
	 * @author sam.king
	 * @date 2018年5月8日
	 * @return
	 */
	@Bean(name = "userShiroRealm")
	public UserShiroRealm userShiroRealm() {
		UserShiroRealm userShiroRealm = new UserShiroRealm();
		return userShiroRealm;
	}

	/**
	 * <p>
	 * 权限管理，配置主要是Realm的管理认证
	 * </p>
	 *
	 * @author sam.king
	 * @date 2018年5月8日
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager(@Qualifier("userShiroRealm") UserShiroRealm userShiroRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSubjectFactory(new DefaultWebSubjectFactory());
		securityManager.setRealm(userShiroRealm);
		securityManager.setCacheManager(new MemoryConstrainedCacheManager());
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * <p>
	 * cookie对象; rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
	 * </p>
	 *
	 * @author sam.king
	 * @date 2018年5月9日
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// 记住我cookie生效时间 ,单位秒
		simpleCookie.setMaxAge(30 * 60);
		return simpleCookie;
	}

	/**
	 * <p>
	 * cookie管理对象; rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
	 * </p>
	 * 
	 * @author sam.king
	 * @date 2018年5月9日
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		String salt = "XxxxController@0";
		cookieRememberMeManager.setCipherKey(salt.getBytes());
		return cookieRememberMeManager;
	}

	/**
	 * <p>
	 * Filter工厂，设置对应的过滤条件和跳转条件
	 * </p>
	 *
	 * @author sam.king
	 * @date 2018年5月8日
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
		filterMap.put("authc", new ShiroAuthenticationFilter());
		shiroFilterFactoryBean.setFilters(filterMap);

		Map<String, String> map = new HashMap<String, String>();
		map.put("/api/**", "anon");
		// 登出
		map.put("/logout", "anon");
		// 对所有用户认证
		map.put("/**", "authc");
		// 登录
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
	}

	/**
	 * <p>
	 * 加入注解的使用，不加入这个注解不生效
	 * </p>
	 *
	 * @author sam.king
	 * @date 2018年5月8日
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * <p>
	 * Shiro生命周期处理器
	 * </p>
	 *
	 * @author sam.king
	 * @date 2018年5月8日
	 * @return
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
		return lifecycleBeanPostProcessor;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

}