package com.northtech.WeChat.test.mapper;

import com.northtech.WeChat.test.bean.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {
    SysUser find(String id);
    SysUser findByMobile(String mobile);
}
