package com.example.demo.shiro.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用的controller
 */
@RestController
@Slf4j
public class TestController {

    /**
     * 测试不需要拦截的
     * @return
     */
    @GetMapping("/api/test")
    public Object annoTest(){
        log.info("api/test");
        return "the request uri is /api/test";
    }

    /**
     * 拦截并校验权限的
     * @return
     */
    @RequiresPermissions("authc:test")
    @GetMapping("/authc/test")
    public Object authcTest(){
        log.info("authc/test");
        return "the request uri is /authc/test";
    }

    /**
     * 拦截校验权限不通过的
     * @return
     */
    @RequiresPermissions("authc:per")
    @GetMapping("/authc/per")
    public Object authcNoPermissionsTest(){
        log.info("authc/per");
        return "the request uri is /authc/per";
    }

    /**
     * 不校验权限的
     * @return
     */
    @GetMapping("/authc/abc")
    public Object authcPermissionsTest(){
        log.info("authc/abc");
        return "the request uri is /authc/abc";
    }

    /**
     * 登录的
     * @param name
     * @return
     */
    @GetMapping("/login")
    public Object loginTest(@RequestParam("name") String name){
        log.info("login");
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, "123456");
        token.setRememberMe(true);
        try {
            subject.login(token);
            jsonObject.put("token", subject.getSession().getId());
            jsonObject.put("msg", "登录成功");
        } catch (IncorrectCredentialsException e) {
            jsonObject.put("msg", "密码错误");
        } catch (LockedAccountException e) {
            jsonObject.put("msg", "登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            jsonObject.put("msg", "该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
