package com.northtech.WeChat;

import com.northtech.J2EE.util.CommonConfigUtil;
import com.northtech.WeChat.wxmetainfo.utils.WXConfigUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
public class WeChatApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(WeChatApplication.class, args);

		CommonConfigUtil.init("common.properties");
        boolean  enable_weixin_funtino = false;
        if (enable_weixin_funtino) {
            WXConfigUtil.init("wxinfo.properties");//微信
            while (!WXConfigUtil.refreshAccessToken()) {
                Thread.sleep(5000);
            }
        }
	}

}
