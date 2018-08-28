package com.northtech.WeChat.wxmetainfo.service;

import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("wxSchedTaskService")
public class WXSchedTaskService {
    private static final Logger logger = LoggerFactory.getLogger(WXSchedTaskService.class);

    @Scheduled(initialDelay =  1600*1000, fixedDelay = 700*1000 )
    public void getWeixinAccessToken() throws InterruptedException {
        logger.info("getWeixinAccessToken begin.");

        while(! WXConfigUtil.refreshAccessToken() ){

            Thread.sleep(5000);

        }


        logger.info("getWeixinAccessToken end.");
    }



}
