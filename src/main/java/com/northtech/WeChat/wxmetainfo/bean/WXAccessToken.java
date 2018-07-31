package com.northtech.WeChat.wxmetainfo.bean;

public class WXAccessToken {
    private String access_token;//接口访问凭证
    private int expires_in;//有效时长
    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    public int getExpires_in() {
        return expires_in;
    }
    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

}