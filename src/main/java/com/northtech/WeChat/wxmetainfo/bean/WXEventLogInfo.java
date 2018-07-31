package com.northtech.WeChat.wxmetainfo.bean;

import com.northtech.WeChat.test.bean.SysUser;

import java.io.Serializable;
import java.util.Date;

public class WXEventLogInfo implements Serializable{

    public final  static  int   CONST_EVENTTYPE_SUBSCRIBE = 1;
    public final  static  int   CONST_EVENTTYPE_NOSUBSCRIBE = 0;

    private String wxOpenId;
    private int  eventType;
    private Date createDate;
    private Date updateDate;


    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }


    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }



}