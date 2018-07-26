package com.northtech.WeChat.wxmetainfo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.northtech.WeChat.wxmetainfo.bean.wxmessage.Article;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.ImageMessage;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.MusicMessage;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.NewsMessage;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.TextMessage;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.VideoMessage;
import com.northtech.WeChat.wxmetainfo.bean.wxmessage.VoiceMessage;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class WXMessageParseUtil {
    /** 
     * 返回消息类型 
     */  
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";  
    public static final String RESP_MESSAGE_TYPE_IMAGE = "image"; 
    public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
    public static final String RESP_MESSAGE_TYPE_VIDEO = "video"; 
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";  
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";  
    /** 
     * 请求消息类型
     */  
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";  
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";  
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";  
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";  
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";  
    public static final String REQ_MESSAGE_TYPE_LINK = "link";   
    /** 
     * 事件
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";  
    /** 
     * 事件类型：关注
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";  
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";  
    /** 
     * 事件类型：CLICK(自己定义菜单点击事件) 
     */  
    public static final String EVENT_TYPE_CLICK = "CLICK"; 
    
    /** 
     * 事件类型：视图
     */
    public static final String  EVENT_TYPE_VIEW = "VIEW";//自定义菜单点击事件
	public static final String  EVENT_TYPE_SCAN = "SCAN";//扫描二维码事件
	public static final String EVENT_TYPE_LOCATION = "LOCATION";//位置事件
	public static final String EVENT_SCANCODE_WAITMSG="scancode_waitmsg";//扫码推事件且弹出“消息接收中”提示框的事件推送

    /** 
     * 解析微信发来的请求（XML） 
     * @throws IOException 
     * @throws DocumentException 
     * 
     *  
     */
    public  static  HashMap<String, String> parseXml(HttpServletRequest request) throws IOException, DocumentException  {  
        // 将解析结果存储在HashMap中  
        HashMap<String, String> map = new HashMap<String, String>();  
        // 从request中取得输入流  
        InputStream inputStream = request.getInputStream();  
       // 读取输入流  
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream); 
        // 得到xml根元素  
        Element root = document.getRootElement();  
        recursiveParseXML(root,map); 
        inputStream.close();//关闭流
        return map;
    }  
    //遍历全部子节点
    private static void recursiveParseXML(Element root,HashMap<String, String> map) throws DocumentException{
   	 @SuppressWarnings("unchecked")
	List<Element> elementList = root.elements();
   	 if(elementList.size()==0){
//			System.out.println(root.getName()+"=>"+root.getTextTrim());
   	     map.put(root.getName(), root.getTextTrim());
   	 
   	 }else{
   		 for(Element element :elementList){
   			 recursiveParseXML(element,map);
   		 }
   	   }
    }
    
    
    /** 
     * 响应消息对象转换成xml 
     * @param textMessage 文本消息对象 
     */  
    public static  String MessageToXml(TextMessage textMessage) {  
        xstream.alias("xml", TextMessage.class);  
        return xstream.toXML(textMessage);  
    }  
    public static String  MessageToXml(MusicMessage musicMessage) {  
        xstream.alias("xml", musicMessage.getClass());  
        return xstream.toXML(musicMessage);  
    }  
    public  static String newsMessageToXml(NewsMessage newsMessage) {  
        xstream.alias("xml", newsMessage.getClass());  
        xstream.alias("item", new Article().getClass());  
        return xstream.toXML(newsMessage);  
    }  
    public  static String  MessageToXml(ImageMessage imageMessage) {  
        xstream.alias("xml", imageMessage.getClass());  
        return xstream.toXML(imageMessage);  
    } 
    public  static String  MessageToXml(VideoMessage videoMessage) {  
        xstream.alias("xml", videoMessage.getClass());  
        return xstream.toXML(videoMessage);  
    }  
    public static  String  MessageToXml(VoiceMessage voiceMessage) {  
        xstream.alias("xml", voiceMessage.getClass());  
        return xstream.toXML(voiceMessage);  
    }  
    /** 
     * 扩展xstream，使其支持CDATA块 
     */  
    private  static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对全部xml节点的转换都添加CDATA标记  
                boolean cdata = true;  
                public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);  
                    if(name.equals("CreateTime")){
                        cdata = false;
                    }
                }  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });
}