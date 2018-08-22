package com.northtech.WeChat.wxmetainfo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("wxaction_menuview")
public class WeChatMenuViewController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatMenuViewController.class);

    @RequestMapping("/bind")
    public  @ResponseBody
    String  bind( HttpServletRequest request,Model model) {
        logger.info(request.getSession().getAttribute("wx_open_id") + " want to bind.");
        model.addAttribute("wx_open_id",request.getSession().getAttribute("wx_open_id"));
        return "/wechat/register";
    }

    @RequestMapping("/unbind")
    public String unbind( HttpServletRequest request ,Model model) {
        String wx_open_id = (String) request.getSession().getAttribute("wx_open_id");
        Boolean isbind = (Boolean) request.getSession().getAttribute("isbind");
        if (isbind){
            //发送模版消息，进行注销
            //wxBindService.UNBind(wx_open_id);
            return "wechat/unbind_ok";
        }
        else{
            model.addAttribute("wx_open_id",request.getSession().getAttribute("wx_open_id"));
            return "/wechat/register";
        }

    }


}
