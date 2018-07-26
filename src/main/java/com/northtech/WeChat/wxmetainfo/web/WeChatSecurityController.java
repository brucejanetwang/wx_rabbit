package com.northtech.WeChat.wxmetainfo.web;

import com.northtech.WeChat.wxmetainfo.utils.SignUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXMessageParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;


public class WeChatSecurityController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatSecurityController.class);
    @RequestMapping("/wx_test")
    public  @ResponseBody   String  test2() {
         return "test";
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
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
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
    public void DoPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> map = WXMessageParseUtil.parseXml(request);
            String msgtype = map.get("MsgType");//event
            if (WXMessageParseUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)) {
                String respXML = eventDispatcher.processEvent(map); // 进入事件处理
                PrintWriter out = response.getWriter();
                System.out.println(respXML);
                out.write(respXML);
                out.close();
                logger.info("进入事件处理完毕");
            } else {
                String respXML = MsgDispatcher.processMessage(map); // 进入消息处理
                PrintWriter out = response.getWriter();
                System.out.println(respXML);
                out.write(respXML);
                out.close();
                logger.info("进入消息处理完毕");
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}