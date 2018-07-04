package com.example.demo.shiro.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * shiro自定义拦截器
 * </p>
 *
 * @author shaohua
 * @date 2017年6月16日
 */
@Slf4j
public class ShiroAuthenticationFilter extends FormAuthenticationFilter {
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		log.info(((HttpServletRequest) request).getRequestURI());
		log.info(request.getContentType());
		if (isLoginRequest(request, response)) {
			log.info("登录的连接，直接过。");
			return true;
		} else {
			log.info("非登录连接验证失败。");
			saveRequest(request);

			response.setCharacterEncoding("UTF-8");
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			JSONObject json = new JSONObject();
			json.put("code", 9);
			json.put("msg", "请重新登录！");
			json.put("url", "/");
			response.getWriter().write(json.toJSONString());
			response.getWriter().flush();
			response.getWriter().close();
			return false;
		}
	}

}
