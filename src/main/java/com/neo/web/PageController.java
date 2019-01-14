package com.neo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/20.
 */
@Controller
public class PageController {
    private final static Logger logger = LoggerFactory.getLogger(PageController.class);
    @RequestMapping("add")
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
}
