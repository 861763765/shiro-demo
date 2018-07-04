package com.example.demo.shiro.config;

import com.example.demo.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

@Slf4j
public class UserShiroRealm extends AuthorizingRealm {

    @Resource
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object obj = principals.getPrimaryPrincipal();
        log.info("obj is {}", obj);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 字符串形式的权限，shiro还支持role形式的
        info.addStringPermission("authc:test");
        // TODO 用户授权信息
        return info;
    }

    // 这个authcToken是在login接口中存进去的
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        log.info("userService is {}", userService);
        // TODO 登录认证
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 通过表单接收的用户名
        String username = token.getUsername();
        if (username != null && !"".equals(username)) {
            Object account = userService.getUser(username);
            if (account != null) {
                return new SimpleAuthenticationInfo(account, "123456", getName());
            } else {
                log.warn("用户不存在：" + username);
                throw new UnknownAccountException();
            }
        }
        return null;
    }
}
