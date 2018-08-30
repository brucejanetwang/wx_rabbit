package com.northtech.WeChat.other.web;


import com.northtech.Common.utils.SMSYunPianUtils;
import com.northtech.J2EE.util.CommonConfigUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("tools")
@RestController
public class ToolsController {

    @RequestMapping("/sendvcode")
    public String SendVCode(@RequestParam("vcode") String vcode, @RequestParam("mobile") String mobile ){

        String signname = "【西安北科】";
        String scontext = signname+"您的验证码是"+vcode;
        int sendtime = 0;
        while(sendtime<3  ){
            if (   (new SMSYunPianUtils()).singleSendMsg(mobile,scontext,CommonConfigUtil.YUNPIAN_SECRET)){
                return "ok";
            }
            sendtime++;
        }
        return "fail";
    }
}


