package com.northtech.WeChat.wxmetainfo.interceptor;

import com.northtech.WeChat.wxmetainfo.bean.WXBindInfo;
import com.northtech.WeChat.wxmetainfo.service.WXBindService;
import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class WXViewUrlInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(WXViewUrlInterceptor.class);
    @Value("${sys.weixin.fake.enable}")
    private boolean weixinfake;

    @Autowired
    WXBindService wxBindService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getLocalAddr();
        String url = request.getRequestURL().toString();
        logger.info("WXViewUrlInterceptor catch ip="+ ip + ",url="+url);
        if (weixinfake){
            WXConfigUtil.checkWXMenuCallBackParse(request,response,true);
            return true;
        }
        Map<String, String[]> parameterMap = request.getParameterMap();

        Boolean checkresult = WXConfigUtil.checkWXMenuCallBackParse(request,response,false);

        if (checkresult){
            String openid = (String) request.getSession().getAttribute("wx_open_id");
            logger.info("get openid="+ openid);
            WXBindInfo wxBindInfo = wxBindService.getUserByWXOpenID(openid);
            if(wxBindInfo == null){
                request.getSession().setAttribute("isbind", false);
            }else{
                request.getSession().setAttribute("sys_user_id", wxBindInfo.getBindUserId());
                request.getSession().setAttribute("isbind", true);
                logger.info("bind openid:"+wxBindInfo.getWxOpenId() + " to user:"+wxBindInfo.getBindUserId().getMobile());
            }
        }else{
            request.getSession().setAttribute("isbind", false);
        }
        return checkresult;
    }

}
