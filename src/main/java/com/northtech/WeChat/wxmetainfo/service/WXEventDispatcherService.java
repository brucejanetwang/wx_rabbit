package com.northtech.WeChat.wxmetainfo.service;

import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.Article;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.NewsMessage;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.TextMessage;
import com.northtech.WeChat.wxmetainfo.utils.DesSecretUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXMessageParseUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class WXEventDispatcherService {
	private static final Logger logger = LoggerFactory.getLogger(WXEventDispatcherService.class);
	@Autowired
	WXBindService wxBindService;
	@Autowired
	WXEventLogService wxEventLogService;
	private String WXBindHttpUrl = "/wx_register/bind";
	private String WXBindPayShopUrl = "/wx_pay_shop/to_pay";
	/**
	 * 处理微信菜单的点击事件 param：微信发来的请求数据集合 xml——>map
	 */
	public String processEvent(Map<String, String> map) {
		String respXML = null;
		TextMessage tm = new TextMessage();
		String WXOpenID = map.get("FromUserName");// 成员变量，对于service只有一个。
		String toUserName = map.get("ToUserName");
		// 响应消息XML
		tm.setFromUserName(toUserName);// 谁发送的
		tm.setToUserName(WXOpenID);// 发送给谁
		tm.setMsgType(WXMessageParseUtil.RESP_MESSAGE_TYPE_TEXT);
		tm.setCreateTime(new Date().getTime());
		if (map.get("Event").equals(WXMessageParseUtil.EVENT_TYPE_SUBSCRIBE)) { // 关注事件
			wxEventLogService.RecordSubscribe(WXOpenID);
			// 首先判断该用户是否第一次关注,通过查找数据库中是否存在该OppenId
			WXBindInfo  wxBindInfo = wxBindService.getUserByWXOpenID(WXOpenID);
			if (wxBindInfo == null) {
				/*
				 * 代表用户首次关注，将其openid保存进数据库，并提示其进行绑定操作
				 */
				String msg = "欢迎关注园区行微信公众平台!园区行公众平台是西安北航园区为了使各个部分高效配合、紧密联系,采用统一的管理方式,建立了一个会员系统。将会员在园区的消费、日常活动详情记录。\n\n"
						+ "&lt;a href=&quot;"
						+ WXBindHttpUrl
						+ "?openid=" + WXOpenID
						+ "&quot;&gt;点此进行绑定&lt;/a&gt;";
				tm.setContent(msg);

				logger.info(WXOpenID + "进入首次关注处理事件");
			} else if (wxBindInfo.getState() == WXBindInfo.CONST_STATE_BIND_NO) {
				/*
				 * 用户再次关注时，判断其是否绑定,若未绑定
				 */
				tm.setContent("欢迎关注园区行微信公众平台!园区行公众平台是西安北航园区为了使各个部分高效配合、紧密联系,采用统一的管理方式,建立了一个会员系统。将会员在园区的消费、日常活动详情记录。\n\n"
						+ "&lt;a href=&quot;"
						+ WXBindHttpUrl
						+ "?openid=" + WXOpenID
						+ "&quot;&gt;点此进行绑定&lt;/a&gt;");
				logger.info(WXOpenID + "进入再次关注处理事件");

			} else {
				tm.setContent("欢迎关注园区行微信公众平台!您已经绑定,请尽情使用");
			}

		}
		else if (map.get("Event").equals(WXMessageParseUtil.EVENT_TYPE_UNSUBSCRIBE)) { // 取消关注事件
			wxEventLogService.RecordUNSubscribe(WXOpenID);

			wxBindService.UNBind(WXOpenID);

			logger.info("进入取消关注处理事件");
		} 
		// =============== 用户未绑定，也可以使用的相关点击菜单功能 start============

		else if (map.get("Event").equals(WXMessageParseUtil.EVENT_TYPE_SCAN)) {
			logger.info("==============这是扫描二维码事件！================");
			// 扫描二维码事件
		}
		else if (map.get("Event").equals(WXMessageParseUtil.EVENT_SCANCODE_WAITMSG)) {
			// 扫描二维码事件
			//不安全，url容易伪造； 解决方法直接过去，用session获取openid； 或者加时间限制
			//目前token 是时间字符串， 需要加一个盐 salt 校验才行
			String result = map.get("ScanResult");//二维码中获取到的商家信息json字符串
			logger.info("==============这是扫描二维码等待事件！================");
			String jsonStr = "";
			try {
				jsonStr = DesSecretUtil.decryptDES(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//发送一个失败模板没有必要，直接返回  
				tm.setContent("非园区二维码，不支持使用!");  
				return WXMessageParseUtil.MessageToXml(tm);
			}
			
			JSONObject object =  JSONObject.fromObject(jsonStr);
			String shopName = object.getString("shopName"); 
			String shopID = object.getString("shopID");
			String type = object.getString("type");
			String cardid = object.getString("cardid");
			String ScanType = map.get("ScanType");
			Long createTime = new Date().getTime()/1000/60 * 7 + 13;
			// 图文信息
			Article article = new Article();
			article.setTitle("支付->"+shopName);
			article.setDescription("点击进入支付界面，30分钟后失效.");
			article.setPicUrl(  "/images/wechatImages/buy.jpg");
			String httpUrl  =WXBindPayShopUrl
					+ "?openid=" + WXOpenID+"&amp;shopName="+shopName
					+"&amp;shopID="+shopID+"&amp;type="+type
					+"&amp;cardid="+cardid   +"&amp;token="+ createTime ;
			article.setUrl(httpUrl);

			List<Article> articles = new ArrayList<Article>();
			articles.add(article);

			NewsMessage nm = new NewsMessage();
			nm.setFromUserName(toUserName);
			nm.setToUserName(WXOpenID);
			nm.setCreateTime(new Date().getTime());
			nm.setMsgType(WXMessageParseUtil.RESP_MESSAGE_TYPE_NEWS);
			nm.setArticleCount(1);
			nm.setArticles(articles);
			respXML = WXMessageParseUtil.newsMessageToXml(nm);
			
			
		}

		else  if (map.get("Event").equals(WXMessageParseUtil.EVENT_TYPE_LOCATION)) {
			// 位置上报事件
			logger.info("==============这是位置上报事件=============");
		}
		else if (map.get("Event").equals(WXMessageParseUtil.EVENT_TYPE_VIEW)) {
			// 自定义菜单View事件
			String eventKey = map.get("EventKey");
			logger.info("自定义菜单View事件,EventKey="+eventKey);
		}
		else if (map.get("Event").equals(WXMessageParseUtil.EVENT_TYPE_CLICK)){
			String eventKey = map.get("EventKey");
			logger.info("自定义菜单Click事件,EventKey="+eventKey);
		}
		else{

		}

		if (respXML == null) {
			respXML = WXMessageParseUtil.MessageToXml(tm);
		}
		return respXML;
	}
}
