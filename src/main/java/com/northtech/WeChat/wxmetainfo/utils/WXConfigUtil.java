package com.northtech.WeChat.wxmetainfo.utils;

import com.northtech.WeChat.wxmetainfo.bean.wxbutton.*;
import net.sf.json.JSONObject;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WXConfigUtil {
	private static final Logger logger = LoggerFactory.getLogger(WXConfigUtil.class);
	private static Configuration configs;
	public  static String APP_ID;// 服务号的应用ID
	public  static String APP_SECRET;// 服务号的应用密钥
	public  static String TOKEN;// 服务号的配置token
	public  static String MCH_ID;// 商户号
	public  static String API_KEY;// API密钥
	public  static String SIGN_TYPE;// 签名加密方式
	public  static String CERT_PATH ;//微信支付证书
	public  static String WXSITE_ROOT_HTTPURL; //微信服务端根路径，后面拼接controller的action路径即可

	public static String ACCESS_TOKEN; //微信接口访问钥匙

	public static String OAUTH2_STATEVALUE; //微信menu url接口state参数值

	public static String PAY_NOTICEURL;
	public static int PAY_EXPIRE_MINS;

	public static synchronized void init(String filePath) {
		if (configs != null) {
			return;
		}
		try {
			configs = new PropertiesConfiguration(filePath);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		if (configs == null) {
			throw new IllegalStateException("can`t find file by path:"
					+ filePath);
		}
		APP_ID = configs.getString("APP_ID");
		APP_SECRET = configs.getString("APP_SECRET");
		TOKEN = configs.getString("TOKEN");
		MCH_ID = configs.getString("MCH_ID");
		API_KEY = configs.getString("API_KEY");
		SIGN_TYPE = configs.getString("SIGN_TYPE");
		CERT_PATH = configs.getString("CERT_PATH");
		WXSITE_ROOT_HTTPURL = configs.getString("WXSITE_ROOT_HTTPURL");
		OAUTH2_STATEVALUE = configs.getString("OAUTH2_STATEVALUE");
		PAY_NOTICEURL =  WXSITE_ROOT_HTTPURL + configs.getString("PAY_NOTICEURL");
		PAY_EXPIRE_MINS = configs.getInt("PAY_EXPIRE_MINS");

	}

	public static synchronized boolean refreshAccessToken() {
		String requestUrl = TOKEN_URL.replace("APPID", APP_ID).replace("APPSECRET", APP_SECRET);
		JSONObject jsonObject;
		try{
			String tokenGetResult =  HttpUtil.httpsGetRequest(requestUrl,"");
			jsonObject = JSONObject.fromObject(tokenGetResult);
		}catch (Exception e){
			return false;
		}

		if(! jsonObject.containsKey("access_token")){
			logger.error("refreshAccessToken error,cause:" + jsonObject.toString());
			return false;
		}
		String accessToken=jsonObject.getString("access_token");
		int expiresIn = jsonObject.getInt("expires_in");
		ACCESS_TOKEN = accessToken;
		logger.info("refreshAccessToken success,expries_in="+expiresIn+",access_token="+accessToken);
		return true;
	}


	public static boolean checkWXMenuCallBackParse(HttpServletRequest request, HttpServletResponse response,boolean fake){
		if(fake){
			request.getSession().setAttribute("wx_open_id", "fake_open_id");
			return true;
		}
		Cookie[] cookies = request.getCookies();
		//判断cookie中是否存在openid 若存在则直接跳过，不存在则获取一次
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("openId")){
					String openid = cookie.getValue();
					request.getSession().setAttribute("wx_open_id", openid);
					return true;
				}
			}
		}
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		if(!state.equals(OAUTH2_STATEVALUE)){
			return false;
		}
		try{
			String openid_url =  OAUTH2_URL;
			String requestUrl = openid_url.replace("APPID", APP_ID).replace("APPSECRET", APP_SECRET).replace("CODE", code);
			String openIDGetResult =  HttpUtil.httpsGetRequest(requestUrl,"");
			JSONObject jsonObject = JSONObject.fromObject(openIDGetResult);
			String user_accessToken=jsonObject.getString("access_token");
			String refresh_token=jsonObject.getString("refresh_token");
			String openid = jsonObject.getString("openid");
			request.getSession().setAttribute("wx_open_id", openid);
			logger.info("success get openid by code.");
			//获取微信用户openid存储在cookie中的信息
			Cookie userCookie=new Cookie("openId",openid);
			userCookie.setMaxAge(-1);
			userCookie.setPath("/");
			response.addCookie(userCookie);

		}catch (Exception e){
			return false;
		}
		return true;
	}


	public  static String getMenuJsonString(){
		//json字符串解析和循环处理
		//菜的按钮触发事件url 前缀
		String wx_oauth2_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + APP_ID;
		String button_http_url_placeholder = "REPLACE_BUS_ACTIONURL";
		String final_button_http_url = wx_oauth2_url + "&redirect_uri=" + WXSITE_ROOT_HTTPURL+ "/wxaction_menuview/" + button_http_url_placeholder + "&response_type=code&scope=snsapi_base&state="+OAUTH2_STATEVALUE+"#wechat_redirect";
		String bus_button_actionurl = "";

		// 1号二级点击菜单

		ViewButton subbtn10 = new ViewButton();
		subbtn10.setName("园区介绍");
		subbtn10.setType("view");
		subbtn10.setUrl("http://wx.xabsp.com/?mod=about");

		ViewButton subbtn11 = new ViewButton();
		subbtn11.setName("企业信息");
		subbtn11.setType("view");
		bus_button_actionurl = "/mobileShowCompanylist";
		subbtn11.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));


		ViewButton subbtn13 = new ViewButton();
		subbtn13.setName("入园指引");
		subbtn13.setType("view");
		subbtn13.setUrl(
				"http://uri.amap.com/marker?markers=108.977174,34.161396,停车场1|108.977598,34.161298,停车场2|108.977657,34.159363,停车场3|108.97821,34.161316,咖啡厅|108.978124,34.161138,餐厅|108.976734,34.161178,健身房|108.977255,34.160734,会议室|108.977877,34.161129,图书馆&src=mypage&coordinate=gaode&callnative=0");
		ViewButton subbtn14 = new ViewButton();
		subbtn14.setName("资讯动态");
		subbtn14.setType("view");
		subbtn14.setUrl("http://wx.xabsp.com/?mod=news");

		ViewButton subbtn15 = new ViewButton();
		subbtn15.setName("微官网");
		subbtn15.setType("view");
		subbtn15.setUrl("http://wx.xabsp.com");

		ComplexButton btn1 = new ComplexButton();
		btn1.setName("园区介绍");
		btn1.setSub_button(new BaseButton[] {subbtn10, subbtn13 ,subbtn14,subbtn11, subbtn15});

		ViewButton subbtn21 = new ViewButton();
		subbtn21.setName("绑定/解除");
		subbtn21.setType("view");
		bus_button_actionurl = "unbind";
		subbtn21.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));

		ViewButton subbtn22 = new ViewButton();
		subbtn22.setName("我要充值");
		subbtn22.setType("view");
		bus_button_actionurl = "/charge";
		subbtn22.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));

		ViewButton subbtn23 = new ViewButton();
		subbtn23.setName("会员信息");
		subbtn23.setType("view");
		bus_button_actionurl = "/person";
		subbtn23.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));

		ClickButton subbtn24 = new ClickButton();
		subbtn24.setName("扫码消费");
		subbtn24.setType("scancode_waitmsg");
		subbtn24.setKey("KEY_SCAN");

		ViewButton subbtn25 = new ViewButton();
		subbtn25.setName("公司缴费");
		subbtn25.setType("view");
		bus_button_actionurl = "/mobileQueryListData";
		subbtn25.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));


		// 一级复合会员菜单
		ComplexButton btn2 = new ComplexButton();
		btn2.setName("会员服务");
		btn2.setSub_button(new BaseButton[] { subbtn21, subbtn22, subbtn23, subbtn24, subbtn25 });

		ViewButton subbtn31 = new ViewButton();
		subbtn31.setName("安防");
		subbtn31.setType("view");
		bus_button_actionurl = "/mobileSmartTest";
		subbtn31.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));


		ViewButton subbtn32 = new ViewButton();
		subbtn32.setName("物业报修");
		subbtn32.setType("view");
		bus_button_actionurl = "/mobilePropertyList";
		subbtn32.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));

		ViewButton subbtn33 = new ViewButton();
		subbtn33.setName("会场预定");
		subbtn33.setType("view");
		bus_button_actionurl = "/mobileMeetingTest";
		subbtn33.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));

		ViewButton subbtn34 = new ViewButton();
		subbtn34.setName("创业服务");
		subbtn34.setType("view");
		bus_button_actionurl = "/mobilePropertyList";
		subbtn34.setUrl(final_button_http_url.replace(button_http_url_placeholder,bus_button_actionurl));


		ComplexButton btn3 = new ComplexButton();
		btn3.setName("园区服务");
		btn3.setSub_button(new BaseButton[] { subbtn31, subbtn32, subbtn33, subbtn34 });



		// 菜单对象
		Menu menu = new Menu();
		menu.setButton(new BaseButton[] { btn1, btn2, btn3 });
		// 将java对象转换成json字符串

		String json = JSONObject.fromObject(menu).toString();// json菜单对象
		logger.info("create menus:"+json);
		return json;
	}

	/**
	 * 微信基础接口地址
	 */
	// 获取token接口(GET)
	public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// oauth2授权接口(GET)
	public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
	// 刷新access_token接口（GET）
	public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	// 菜单创建接口（POST）
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单查询（GET）
	public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 菜单删除（GET）
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/**
	 * 微信支付接口地址
	 */
	// 微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 微信退款接口(POST)
	public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 订单查询接口(POST)
	public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 关闭订单接口(POST)
	public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	// 退款查询接口(POST)
	public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	// 对账单接口(POST)
	public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	// 短链接转换接口(POST)
	public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	// 接口调用上报接口(POST)
	public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

}