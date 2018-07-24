package com.northtech.WeChat.test.service;

import com.northtech.WeChat.test.bean.SysUser;
import com.northtech.WeChat.test.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service("SysUserService")
@ComponentScan({"com.northtech.WeChat.test.mapper"})
public class SysUserService {
    @Autowired SysUserMapper sysUserMapper;

    public SysUser find(int id) {
        return sysUserMapper.find(id);
    }
}
