package com.northtech.WeChat.wxmetainfo.bean.pay;

import java.io.Serializable;
import java.util.Date;

public class WXChargePay implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String outTradeNo;
    private String  chargeType;
    private String openId;
    private Integer totalFee;
    private String tradeKeyInfo;
    private String mchId;
    private Date timeStart;
    private String pay_method;
    private Integer status;
    private Date timeEnd;
    private String cardNum;
    private Long  preBalance;
    private Long curBalance;
    private String sign;

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

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }


    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeKeyInfo() {
        return tradeKeyInfo;
    }

    public void setTradeKeyInfo(String tradeKeyInfo) {
        this.tradeKeyInfo = tradeKeyInfo;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
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

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Long getPreBalance() {
        return preBalance;
    }

    public void setPreBalance(Long preBalance) {
        this.preBalance = preBalance;
    }

    public Long getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(Long curBalance) {
        this.curBalance = curBalance;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    private String delFlag;
    private Date updateDate;
}
