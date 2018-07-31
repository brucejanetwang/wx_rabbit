package com.northtech.WeChat.wxmetainfo.mapper;

import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import com.northtech.WeChat.wxmetainfo.bean.WXEventLogInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WXEventLogMapper {
    WXEventLogInfo findHistory(@Param("wxOpenID") String wxOpenID);

    void RecordSubscribe(@Param("wxOpenID") String wxOpenID);

    void RecordUNSubscribe(@Param("wxOpenID") String wxOpenID);
}
