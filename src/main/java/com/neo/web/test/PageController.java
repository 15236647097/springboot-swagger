package com.neo.web.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
