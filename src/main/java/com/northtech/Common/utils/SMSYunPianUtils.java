package com.northtech.Common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.northtech.J2EE.util.CommonConfigUtil;
import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMSYunPianUtils {
		private static Logger logger = LoggerFactory.getLogger(SMSYunPianUtils.class); 
	    private static final Boolean IS_WITH_PROXY = false;

	    /**
	     * 单发短信
	     * 
	     * @param phoneNo
	     * @param text
	     */
	    /*
	     * { "code": 0, "msg": "发送成功", "count": 1, //成功发送的短信计费条数 "fee": 0.05, //扣费条数，70个字一条，超出70个字时按每67字一条计 "unit": "RMB",
	     * // 计费单位 "mobile": "13200000000", // 发送手机号 "sid": 3310228982 // 短信ID }
	     */
	    public boolean singleSendMsg(String phoneNo, String text ,String apiKey)
	    {
	        // 请求参数集合
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("apikey", apiKey);
	        params.put("text", text);
	        params.put("mobile", phoneNo);
	        String jsonstr =  post("https://sms.yunpian.com/v2/sms/single_send.json", params);
	        JSONObject jsonobj = JSONObject.fromObject(jsonstr);
	        if (jsonobj.containsKey("code") && jsonobj.getString("code").equals("0")){
	        	return true;
			}
			return false;

	    }

	

	    private String post(String url, Map<String, String> params)
	    {
	        CloseableHttpClient httpclient = HttpClients.createDefault();
	        CloseableHttpResponse response = null;
	        String respJson = null;
	        try
	        {
	            HttpPost request = new HttpPost(url);
	            // 在公司内网需要设置代理
	            if (IS_WITH_PROXY)
	            {
	                HttpHost proxy = new HttpHost("proxy.XXXX.com.cn", 80, "http");
	                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
	                request.setConfig(config);
	            }
	            // 设置参数
	            List<NameValuePair> list = new ArrayList<NameValuePair>();
	            Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
	            while (iterator.hasNext())
	            {
	                Entry<String, String> elem = (Entry<String, String>) iterator.next();
	                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
	            }
	            if (list.size() > 0)
	            {
	                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
	                request.setEntity(entity);
	            }
	            logger.info("Executing request " + request.getRequestLine() + " to "
	              + request.getURI().toString()  );

	            response = httpclient.execute(request);

	            // 发送短信请求后，返回的json结果信息
	            respJson = EntityUtils.toString(response.getEntity());
	            logger.info("---------------send finished----------------------");
	            logger.info("response status: ", response.getStatusLine());
	            logger.info(respJson);

	        }
	        catch (Exception e)
	        {
	            logger.error("", e);
	            return null;
	        }
	        finally
	        {
	            if (response != null)
	            {
	                try
	                {
	                    response.close();
	                }
	                catch (IOException e)
	                {
	                    logger.error("", e);
	                }
	            }
	            try
	            {
	                httpclient.close();
	            }
	            catch (IOException e)
	            {
	                logger.error("", e);
	            }
	        }
	        return respJson;
	    }

	     public static void main(String[] agr)
	     {
	     	String to_mobile = "18609288376";
	    	 String signname = "【西安北科】";
	    	 String scontext = signname+"您的验证码是1234";
	    	 int sendtime = 0;
	    	 while(sendtime<3 &&  !(new SMSYunPianUtils()).singleSendMsg(to_mobile,scontext, CommonConfigUtil.YUNPIAN_SECRET) ){
			 		sendtime++;
			 }
	     }
}
