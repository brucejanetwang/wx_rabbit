package com.northtech.WeChat.wxmetainfo.utils;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.*;

class MyWXPayConfig  implements WXPayConfig
{
    private byte[] certData;
    private String appid;
    private String mch_id;
    private String api_key;
    private String certPath;

    public MyWXPayConfig()
            throws Exception
    {
        File file = new File(this.certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int)file.length()];
        certStream.read(this.certData);
        certStream.close();
    }


    public MyWXPayConfig(String appid, String mch_id,String api_key, String certPath)
            throws IOException
    {
        this.appid = appid;
        this.mch_id = mch_id;
        this.api_key = api_key;
        this.certPath = certPath;
        if (certPath.length() > 0) {
            File file = new File(certPath);
            InputStream certStream = new FileInputStream(file);
            this.certData = new byte[(int)file.length()];
            certStream.read(this.certData);
            certStream.close();
        }
    }




    public String getAppID()
    {
        return this.appid;
    }


    public String getMchID()
    {
        return this.mch_id;
    }


    public String getKey()
    {
        return this.api_key;
    }


    public InputStream getCertStream()
    {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs()
    {
        return 5000;
    }


    public int getHttpReadTimeoutMs()
    {
        return 5000;
    }


}