package com.northtech.WeChat.test.web;

import net.sf.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import net.sf.json.JSONObject;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/rest/fortest")
    public JSONObject forTest(String name){
        JSONObject obj = new JSONObject();
        obj.put("result",  "hello," + name);
        return obj;
    }
    @RequestMapping("/rest/test")
    public JSONObject test(){
        //post json数据
        JSONObject postData = new JSONObject();
        postData.put("name", "bruce wang");
        String url = "http://127.0.0.1:8081/rest/fortest";
        return   restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
    }


}
