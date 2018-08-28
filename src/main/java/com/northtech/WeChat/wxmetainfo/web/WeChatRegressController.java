package com.northtech.WeChat.wxmetainfo.web;

import com.northtech.WeChat.test.bean.SysUser;
import com.northtech.WeChat.test.service.SysUserService;
import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import com.northtech.WeChat.wxmetainfo.service.WXBindService;
import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("wxaction_register")
public class WeChatRegressController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatRegressController.class);

    @Autowired
    WXBindService wxBindService;
    @Autowired
    SysUserService sysUserService;

    @RequestMapping("/save_bind")
    public String save_bind( HttpServletRequest request ,Model model) {
        String wx_open_id = (String) request.getSession().getAttribute("wx_open_id");
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (! parameterMap.containsKey("wx_open_id")){
            return "/sys/common_error";
        }
        String req_wx_open_id = parameterMap.get("wx_open_id")[0];
        String mobile = parameterMap.get("mobile")[0];

        if (!wx_open_id.equals(req_wx_open_id)){
            return "/sys/common_error";
        }
        SysUser sysUser = sysUserService.findByMobile(mobile);
        if (sysUser == null){
            model.addAttribute("error","账户不存在");
            return "/wechat/register";
        }
        wxBindService.Bind(wx_open_id,sysUser.getId());
        model.addAttribute("wx_open_id",wx_open_id);
        return "/wechat/bind_ok";
    }



}
