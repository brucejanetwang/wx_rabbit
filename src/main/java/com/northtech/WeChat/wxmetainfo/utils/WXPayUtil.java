package com.northtech.WeChat.wxmetainfo.utils;


import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import com.github.wxpay.sdk.WXPay;

public class WXPayUtil {
    private static final Logger logger = LoggerFactory.getLogger(WXPayUtil.class);


    public static String getNow(Date date) {
        SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdft.format(date);
    }

    public static String addMin(Date date, int limitTime) {
        SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(date);
        nowTime.add(Calendar.MINUTE, limitTime);
        return sdft.format(nowTime.getTime());
    }


    public static String createBrandWCPayRequestData(String app_id, String mch_id, String api_key,
                                                     String trade_key_info, String total_fee, String out_trade_no,
                                                     String trade_type, String openid, String notify_url,
                                                     int time_expire_mins,String signType)  {

        JSONObject unifiedOrder = null;
        try {
            unifiedOrder = unifiedOrder(app_id, mch_id, api_key, trade_key_info,
                    total_fee, out_trade_no, "JSAPI", openid, notify_url, time_expire_mins);
        } catch (Exception e) {
            logger.error("createBrandWCPayRequestData error,for unifiedOrder");
            e.printStackTrace();
            return "{\"status\":\"fail\",\"errorCode\":\"prepay_id is null\"}";
        }

        if (unifiedOrder == null || ! unifiedOrder.getString("status").equals("success")) {
            return "{\"status\":\"fail\",\"errorCode\":\"prepay_id is null\"}";
        }

        String prepay_id = unifiedOrder.getString("prepay_id");
        String nonce_str = unifiedOrder.getString("nonce_str");


        SortedMap<Object, Object> packageParams = new TreeMap();
        packageParams.put("appId", app_id);
        packageParams.put("nonceStr", nonce_str);
        packageParams.put("timeStamp",Long.toString(System.currentTimeMillis() / 1000L));
        packageParams.put("package", "prepay_id=" + prepay_id);
        packageParams.put("signType", signType);
        String paySign = createSign("UTF-8", packageParams, api_key);
        packageParams.put("paySign", paySign);

        JSONObject obj = JSONObject.fromObject(packageParams);
        JSONObject resultObj = new JSONObject();
        resultObj.put("jsParam",obj);
        resultObj.put("status", "success");
        return resultObj.toString();
    }


    public static JSONObject unifiedOrder(String app_id, String mch_id, String api_key,
                                          String trade_key_info, String total_fee, String out_trade_no,
                                          String trade_type, String openid, String notify_url,
                                          int time_expire_mins) throws Exception {
        String certPath = "";
        MyWXPayConfig config = new MyWXPayConfig(app_id, mch_id, api_key, certPath);
        WXPay wxpay = new WXPay(config);
        Map<String, String> reqData = new HashMap();

        reqData.put("appid", app_id);
        reqData.put("mch_id", mch_id);

        if ((openid != null) && (openid.length() > 0)) {
            reqData.put("openid", openid);
        }
        reqData.put("body", trade_key_info);
        reqData.put("out_trade_no", out_trade_no);
        reqData.put("total_fee", total_fee);

        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        reqData.put("spbill_create_ip", hostAddress);

        reqData.put("notify_url", notify_url);
        reqData.put("trade_type", trade_type);
        Date date = new Date();
        reqData.put("time_start", getNow(date));
        reqData.put("time_expire", addMin(date, time_expire_mins));

        logger.info("WxPayUtil--->unifiedOrder--->发送微信参数:" + reqData.toString());
        Map<String, String> unifiedOrder = wxpay.unifiedOrder(reqData, 5000, 5000);
        logger.info("WxPayUtil--->unifiedOrder--->" + unifiedOrder.toString());

        String return_code = (String) unifiedOrder.get("result_code");
        if ("SUCCESS".equals(return_code)) {
            if ("SUCCESS".equals(unifiedOrder.get("return_code"))) {
                JSONObject back = new JSONObject();
                back.put("status", "success");
                back.put("prepay_id", unifiedOrder.get("prepay_id"));
                back.put("nonce_str", unifiedOrder.get("nonce_str"));
                if (trade_type.equals("NATIVE")) {  //一般是JSAPI
                    back.put("code_url", unifiedOrder.get("code_url"));
                }
                return back;
            }
            String err_code = (String) unifiedOrder.get("err_code");
            JSONObject back = new JSONObject();
            back.put("status", "fail");
            back.put("errorCode", err_code);
            return back;
        }

        String return_msg = (String) unifiedOrder.get("return_msg");
        JSONObject back = new JSONObject();
        back.put("status", "fail");
        back.put("errorCode", return_msg);
        return back;
    }

    public static String getRequestXml(SortedMap<Object, Object> parameters)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (("attach".equalsIgnoreCase(k)) || ("body".equalsIgnoreCase(k)) || ("sign".equalsIgnoreCase(k))) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }


    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY)
    {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ((!"sign".equals(k)) && (v != null) && (!"".equals(v))) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + API_KEY);
        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();
        return tenpaySign.equals(mysign);
    }



    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String createSignFromMap(String characterEncoding,Map<String, String> reqData , String API_KEY)
    {
        SortedMap<Object, Object> packageParams = new TreeMap();
        Set<String> keySet = reqData.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        for (String k : keyArray) {
            if (reqData.get(k).trim().length() > 0) {
                // 参数值为空，则不参与签名
                packageParams.put(k,reqData.get(k));
            }
        }
        return createSign(characterEncoding,packageParams,API_KEY);
    }


    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY)
    {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ((v != null) && (!"".equals(v)) && (!"sign".equals(k)) && (!"key".equals(k))) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

}
