package com.northtech.WeChat.wxmetainfo.bean.pay;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("business_wx_pay_transaction")
public class WXPayTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String transactionId;
    private String outTradeNo;
    private String tradeType;
    private String openId;
    private Integer realCharge;
    private String mchId;
    private Integer status;
    private Date timeEnd;
    private String delFlag;
    private Date updateDate;

}
