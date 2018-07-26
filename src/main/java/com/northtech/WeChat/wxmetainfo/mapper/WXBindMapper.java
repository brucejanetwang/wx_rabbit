package com.northtech.WeChat.wxmetainfo.mapper;

import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WXBindMapper {
    WXBindInfo getUserByWXOpenID(String WXOpenID);
}
