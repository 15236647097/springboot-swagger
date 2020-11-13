package com.neo.web.test;

import com.alibaba.fastjson.JSONObject;
import com.neo.entity.Bb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {
    private final static Logger logger = LoggerFactory.getLogger(PageController.class);
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String add(){
        System.out.println(123);
        return "add.jsp";
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    @ResponseBody
    public String efficientFrontier(String reqData){
        System.out.println(reqData);
        return "a";
    }

    @RequestMapping(value = "/test1",method = RequestMethod.POST)
    @ResponseBody
    public String efficientFrontier(@RequestBody Bb bb){
        System.out.println(bb);
        return "bb";
    }

    @RequestMapping(value = "/test2",method = RequestMethod.POST)
    @ResponseBody
    public String efficientFrontier1(Bb bb){
        System.out.println(bb);
        if ("1".equals(bb.getBb())){
            return "{\"ciphertext\":1}";
        } else {
            return "{\"ciphertext\":2}";
        }
    }


    public static void main(String[] args) {
        String a = "{\"token\":\"55b3c8dd-f19b-4e65-a63b-a9dfd09fdc1b\",\"holderCode\":\"41010220020815676X\",\"licenseNum\":\"41010220200907001\"}";
        System.out.println(a.length());
        System.out.println(a.substring(2,a.length()-2));
        System.out.println(a.substring(2,a.length()-2).replaceAll("\",\"","&").replaceAll("\":\"","="));
    }
}
