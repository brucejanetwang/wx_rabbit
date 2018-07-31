package com.northtech.WeChat.wxmetainfo.mapper;

import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WXBindMapper {
    WXBindInfo getUserByWXOpenID(@Param("wxOpenID") String wxOpenID);

    void UNBind(@Param("wxOpenID") String wxOpenID);

    void Bind(@Param("wxOpenID")String wxOpenID, @Param("sysUserID")String sysUserID);
}
