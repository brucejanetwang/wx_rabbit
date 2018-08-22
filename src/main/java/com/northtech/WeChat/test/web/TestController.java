package com.northtech.WeChat.test.web;

import com.northtech.WeChat.test.bean.SysUser;
import com.northtech.WeChat.test.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement  // 需要事务的时候加上
@RestController
public class TestController {
    @Autowired
    SysUserService sysUserService;
    private static int id = 0;

    @RequestMapping("/test")
    public Integer test() {
        id++;
        return id;
    }

    @RequestMapping("/test2")
    public SysUser test2() {
        return sysUserService.find("1");
    }

    @RequestMapping("/")
    public String defaultaction() {
        return "hello";
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public Object sessions (HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("message", request.getSession().getAttribute("map"));
        return map;
    }

}