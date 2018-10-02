package com.northtech.WeChat.wxmetainfo.bean.pay;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("business_wx_charge_pay")
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
    private String delFlag;
    private Date updateDate;
}
