package com.example.demo.shiro.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * 测试用的service
 */
@Service
public class UserService {

    public Object getUser(String name) {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("password", "123456");
        return obj;
    }
}
