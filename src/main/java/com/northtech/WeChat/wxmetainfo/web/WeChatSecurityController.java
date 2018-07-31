package com.northtech.WeChat.wxmetainfo.web;

import com.northtech.WeChat.wxmetainfo.service.WXEventDispatcherService;
import com.northtech.WeChat.wxmetainfo.utils.HttpUtil;
import com.northtech.WeChat.wxmetainfo.utils.SignUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXMessageParseUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;


@Controller
public class WeChatSecurityController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatSecurityController.class);

    @Autowired
    WXEventDispatcherService wxEventDispatcherService;

    @RequestMapping("/wx_createmenu")
    public  @ResponseBody   String  wxCreateMenu() {

        String requestUrl =  WXConfigUtil.MENU_CREATE_URL.replace("ACCESS_TOKEN", WXConfigUtil.ACCESS_TOKEN );
        String menujson = WXConfigUtil.getMenuJsonString();

        String respJSON =  HttpUtil.httpsPostRequest(requestUrl,  menujson);
        JSONObject jsonObject  =	JSONObject.fromObject(respJSON);

        if(null != jsonObject){
            int errCode = jsonObject.getInt("errcode");
            String errMsg = jsonObject.getString("errmsg");
            if(0==errCode){

                return "菜单创建成功,返回值："+errMsg;
            }else{

                return "菜单创建失败,原因："+errMsg;
            }
        }
        return "";

    }


    @RequestMapping("/wx_test")
    public  @ResponseBody   String  test2() {
         return "test五三二姨";
    }

    @RequestMapping(value = "wx_security", method = RequestMethod.GET)
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (SignUtil.CheckSignature(signature, timestamp, nonce)) {

                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("WeChatSecurityController security find invalid requests!");
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }


    /**
     * @Description: 接收微信端消息处理并做分发
     * @param @param request
     * @param @param response
     */
    @RequestMapping(value = "wx_security", method = RequestMethod.POST)
    public void DoPost(HttpServletRequest request, HttpServletResponse response
                ,@RequestParam(value = "signature", required = true) String signature,
                       @RequestParam(value = "timestamp", required = true) String timestamp,
                       @RequestParam(value = "nonce", required = true) String nonce

    ) {
        response.setCharacterEncoding("UTF-8");
        if (! SignUtil.CheckSignature(signature, timestamp, nonce)) {
            return;
        }
        try {
            Map<String, String> map = WXMessageParseUtil.parseXml(request);
            String ToUserName = map.get("ToUserName");
            if (!ToUserName.equals("111")){    //"ToUserName" -> "gh_e9b5ea91b2f5"
                throw  new IllegalArgumentException("not invalid paramter.");
            }

            String msgtype = map.get("MsgType");//event
            if (WXMessageParseUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)) {
                logger.info("进入事件处理开始");
                String respXML = wxEventDispatcherService.processEvent(map); // 进入事件处理
                PrintWriter out = response.getWriter();
                System.out.println(respXML);
                out.write(respXML);
                out.close();
                logger.info("进入事件处理完毕");
            } else {
                logger.info("进入消息处理开始");

                logger.info("进入消息处理完毕");
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}