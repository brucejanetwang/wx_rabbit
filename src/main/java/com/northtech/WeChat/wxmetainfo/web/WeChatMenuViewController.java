package com.northtech.WeChat.wxmetainfo.web;

import com.northtech.Common.utils.RandomMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("wxaction_menuview")
public class WeChatMenuViewController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatMenuViewController.class);

    @RequestMapping("/unbind")
    public String unbind( HttpServletRequest request ,Model model,
                          @Value("${sys.mobile.vcode.number}")  int number,
                          @Value("${sys.mobile.vcode.type}")  int type) {
        String wx_open_id = (String) request.getSession().getAttribute("wx_open_id");
        Boolean isbind = (Boolean) request.getSession().getAttribute("isbind");
        if (isbind){
            //发送模版消息，进行注销
            //wxBindService.UNBind(wx_open_id);
            return "wechat/unbind_ok";
        }
        else{
            model.addAttribute("wx_open_id",request.getSession().getAttribute("wx_open_id"));
            String vcode=  (new RandomMaker()).create(type,number);
            model.addAttribute("vcode",vcode);
            return "/wechat/register";
        }

    }



    @RequestMapping("/charge")
    public String charge(HttpServletRequest request ,Model model){
        logger.info(request.getSession().getAttribute("wx_open_id") + " want to bind.");
        String wx_open_id = (String) request.getSession().getAttribute("wx_open_id");
        model.addAttribute("balance",100);
        model.addAttribute("amount",8);
        model.addAttribute("wx_open_id",wx_open_id);
        model.addAttribute("personName","王联辰");
        model.addAttribute("CardNum","P_1232323");
        //个人中心查看账户余额
        return "/wechat/membership_card_charge";
    }
    @RequestMapping("/person")
    public String person(HttpServletRequest request ,Model model){
        logger.info(request.getSession().getAttribute("wx_open_id") + " want to bind.");
        String wx_open_id = (String) request.getSession().getAttribute("wx_open_id");
        model.addAttribute("balance",100);
        model.addAttribute("wx_open_id",wx_open_id);
        model.addAttribute("personName","王联辰");
        model.addAttribute("CardNum","P_1232323");
        //个人中心查看账户余额
        return "/wechat/membership_card_charge";
    }




}
