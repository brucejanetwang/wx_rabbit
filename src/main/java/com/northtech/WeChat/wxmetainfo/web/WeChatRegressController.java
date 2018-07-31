package com.northtech.WeChat.wxmetainfo.web;

import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("wxaction_register")
public class WeChatRegressController {



    @RequestMapping("/bind")
    public  @ResponseBody
    String  bind() {
        return "bind";
    }


    @RequestMapping(value = "unbind", method = RequestMethod.GET)
    public String unbind( HttpServletRequest request) {
       Boolean checkresult = WXConfigUtil.checkWXMenuCallBackParse(request);
       if (checkresult){

       }else{

       }
       return "";
    }


}
