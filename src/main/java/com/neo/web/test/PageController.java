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

    @RequestMapping(value = "/test1",method = RequestMethod.POST,consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public String efficientFrontier(@RequestBody Bb bb){
        System.out.println(bb);
        return "bb";
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("aaa","aaa");
        map.put("aaa1","aaa1");
        System.out.println(JSONObject.toJSONString(map));
    }

}
