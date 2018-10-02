package com.northtech.WeChat.wxmetainfo.bean.wxevent;

import com.northtech.WeChat.test.bean.SysUser;
import java.io.Serializable;
import java.util.Date;

public class WXBindInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    public final  static  int   CONST_STATE_BIND_YES = 1;
    public final  static  int   CONST_STATE_BIND_NO = 0;

    private String wxOpenId;
    private SysUser bindUserId;
    private int  state;
    private Date createDate;
    private Date updateDate;


    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public SysUser getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(SysUser bindUserId) {
        this.bindUserId = bindUserId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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