package com.northtech.WeChat.wxmetainfo.service;

import com.northtech.Common.utils.XMLUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXPayUtil;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Service("wxPayService")
public class WXPayService {

    private static final Logger logger = LoggerFactory.getLogger(WXPayService.class);

    public String imp_wx_pay_notice(SortedMap<Object, Object> packageParams){
        // 根据订单号进行判断
        String transaction_id = (String)packageParams.get("transaction_id");
        // 根据订单号进行判断
        String out_trade_no = (String)packageParams.get("out_trade_no");

        String orderstate =  "sucess"; //orderStateService.selectState(out_trade_no,transaction_id);
        if (orderstate != null && orderstate.equals("sucess")) {
            //已经处理过了
            SortedMap<Object, Object> params = new TreeMap();
            params.put("return_code", "SUCCESS");
            params.put("return_msg", "OK");
            String requestXml = WXPayUtil.getRequestXml(params);
            logger.info("notify----> 支付反馈成功，订单号:" + out_trade_no);
            return requestXml;
        }

        if (WXPayUtil.isTenpaySign("UTF-8", packageParams, WXConfigUtil.API_KEY))
        {
            String resXml = "";
            if ("SUCCESS".equals((String)packageParams.get("result_code"))){
                //支付成功处理

            }else{
                //支付失败处理
                SortedMap<Object, Object> params = new TreeMap();
                params.put("return_code", "FAIL");
                params.put("return_msg", packageParams.get("err_code") );
                String requestXml = WXPayUtil.getRequestXml(params);
                logger.info("notify----> 支付失败,错误信息：" + packageParams.get("err_code"));
                return requestXml;
            }

        }else{
            //签名失败的处理
            SortedMap<Object, Object> params = new TreeMap();
            params.put("return_code", "FAIL");
            params.put("return_msg", "通知签名验证失败" );
            String requestXml = WXPayUtil.getRequestXml(params);
            logger.info("notify---->通知签名验证失败,微信配置参数有误");
            return requestXml;
        }

        logger.info("imp_wx_pay_notice--->notify----> 支付失败, 服务器逻辑错误");
        return  "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[服务器异常]]></return_msg></xml> ";
    }

    public String proces_wx_pay_notice(HttpServletRequest req) {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream inputStream = req.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String s;
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();


            Map<String, String> m = new HashMap();
            m = XMLUtil.doXMLParse(sb.toString());
            logger.info("UpController--->notify---->请求参数: " + m);

            SortedMap<Object, Object> packageParams = new TreeMap();
            Iterator it = m.keySet().iterator();
            while (it.hasNext()) {
                String parameter = (String) it.next();
                String parameterValue = (String) m.get(parameter);
                String v = "";
                if (parameterValue != null) {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            String notify = this.imp_wx_pay_notice(packageParams);
            logger.info("WXPayService--->notify--->返回参数:" + notify);
            return notify;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[服务器异常]]></return_msg></xml> ";

            logger.info("WXPayService--->notify----> 支付失败,UnsupportedEncodingException 信息异常！异常信息为:" + e.getMessage());
            return resXml;
        } catch (IOException e) {
            e.printStackTrace();
            String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[服务器异常]]></return_msg></xml> ";

            logger.info("WXPayService--->notify----> 支付失败,IOException 信息异常！异常信息为:" + e.getMessage());
            return resXml;
        } catch (JDOMException e) {
            e.printStackTrace();
            String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[服务器异常]]></return_msg></xml> ";

            logger.info("WXPayService--->notify----> 支付失败,JDOMException 信息异常！异常信息为:" + e.getMessage());
            return resXml;
        } catch (Exception e) {
            e.printStackTrace();
            String resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[服务器异常]]></return_msg></xml> ";

            logger.info("WXPayService--->notify----> 支付失败,Exception 信息异常！异常信息为:" + e.getMessage());
            return resXml;
        }
    }


}
