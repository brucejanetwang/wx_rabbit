package com.northtech.WeChat.wxmetainfo.service;

import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import com.northtech.WeChat.wxmetainfo.mapper.WXBindMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("wxBindService")
public class WXBindService {
    @Autowired
    WXBindMapper wxBindMapper;

    @Transactional(readOnly = true)
    public WXBindInfo getUserByWXOpenID(String WXOpenID){
        return  wxBindMapper.getUserByWXOpenID(WXOpenID);
    }

    public void UNBind(String wxOpenID) {
        wxBindMapper.UNBind(wxOpenID);
    }
    public void Bind(String wxOpenID,String sysUserID) {
        wxBindMapper.Bind(wxOpenID,sysUserID);
    }
}
