package com.northtech.WeChat.wxmetainfo.bean.pay;

import java.io.Serializable;
import java.util.Date;

public class WXPayTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String transactionId;
    private String outTradeNo;
    private String tradeType;
    private String openId;
    private Integer realCharge;
    private String mchId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    private Integer status;
    private Date timeEnd;
    private String delFlag;
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOpenId() {
        return openId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }


    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getRealCharge() {
        return realCharge;
    }

    public void setRealCharge(Integer realCharge) {
        this.realCharge = realCharge;
    }



    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
