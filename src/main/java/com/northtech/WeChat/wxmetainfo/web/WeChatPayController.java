package com.northtech.WeChat.wxmetainfo.web;

import com.northtech.Common.utils.RandomMaker;
import com.northtech.Common.utils.XMLUtil;
import com.northtech.WeChat.test.service.SysUserService;
import com.northtech.WeChat.wxmetainfo.service.WXBindService;
import com.northtech.WeChat.wxmetainfo.service.WXPayService;
import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXPayUtil;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Controller
public class WeChatPayController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatPayController.class);

    @Value("${wx.pay.fee.ratio:100}")
    private  int wx_pay_fee_ratio;

    @Autowired
    WXPayService wxPayService;


    @ResponseBody
    @RequestMapping(value = {"/wx_pay_notice_action"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST}, produces = {"text/html; charset=utf-8"})
    public String wx_pay_notice(HttpServletRequest req) {
        return wxPayService.proces_wx_pay_notice(req);
    }


    @ResponseBody
    @RequestMapping("/wxaction_pay/get_prepay_info")
    public String get_prepay_info(HttpServletRequest request, Model model) {
        //放到InterceptorConifg中
        String wx_open_id = (String) request.getSession().getAttribute("wx_open_id");
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.containsKey("wx_open_id")) {
            return "/sys/common_error";
        }
        String req_wx_open_id = parameterMap.get("wx_open_id")[0];
        if (!wx_open_id.equals(req_wx_open_id)) {
            return "/sys/common_error";
        }

        String amount = parameterMap.get("amount")[0];
        String card_num = parameterMap.get("card_num")[0];
        String trade_key_info = "会员卡充值"+amount+"元";
        Double douPayCharge = Double.valueOf(amount);
        double pay = douPayCharge.doubleValue() * this.wx_pay_fee_ratio;
        String total_fee = String.valueOf((int)pay);
        String out_trade_no = WXConfigUtil.WX_CHARGE_TRADEINFO_PREX  + (new RandomMaker().create(RandomMaker.TYPE_TIMENUMBER, 6));
        String trade_type = "JSAPI";

        wxPayService.saveChargePay(trade_key_info,trade_key_info,wx_open_id,WXConfigUtil.MCH_ID,card_num);

        return WXPayUtil.createBrandWCPayRequestData(WXConfigUtil.APP_ID, WXConfigUtil.MCH_ID, WXConfigUtil.API_KEY,
                trade_key_info, total_fee, out_trade_no, trade_type, wx_open_id,
                WXConfigUtil.PAY_NOTICEURL, WXConfigUtil.PAY_EXPIRE_MINS,WXConfigUtil.SIGN_TYPE);

    }


}
