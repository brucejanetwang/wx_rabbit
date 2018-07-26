package com.northtech.WeChat.wxmetainfo.service;


import com.northtech.WeChat.wxmetainfo.mapper.WXEventLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("wxEventLogService")
public class WXEventLogService {
    @Autowired
    WXEventLogMapper wxEventLogMapper;
    @Transactional(readOnly = false)
    public void RecordSubscribe(String wxOpenID) {
        wxEventLogMapper.RecordSubscribe(wxOpenID);
    }

    @Transactional(readOnly = false)
    public void RecordUNSubscribe(String wxOpenID) {
        wxEventLogMapper.RecordUNSubscribe(wxOpenID);
    }
}