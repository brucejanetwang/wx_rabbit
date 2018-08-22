package com.northtech.WeChat.test.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ViewController {
    @RequestMapping("/view_test")
    public  String view_test(HttpServletRequest request, Model model){
        model.addAttribute("wx_open_id",12211);
        return "/wechat/register";


    }
    @RequestMapping("/view_test2")
    public  String view_test2(HttpServletRequest request, Model model){
        Map<String, String[]> parameterMap = request.getParameterMap();
        model.addAttribute("test2","222");
        return "/test/test";
    }


}
