package com.neo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import util.MVO;

/**
 * Created by wangyinuo on 2017/11/20.
 */
@Controller
public class PageController {
    @RequestMapping("add")
    public String add(){
        return "add.jsp";
    }

    @RequestMapping("/efficientFrontier1")
    @ResponseBody
    public Object[] efficientFrontier(){
        Object[] resust = null;
        Double [] ExpReturn = { 0.0054, 0.0531, 0.0779, 0.0934, 0.0130 };
        Double[][] ExpCovariance = {{0.0569,  0.0092,  0.0039,  0.0070,  0.0022},
                {0.0092,  0.0380,  0.0035,  0.0197,  0.0028},
                {0.0039,  0.0035,  0.0997,  0.0100,  0.0070},
                {0.0070,  0.0197,  0.0100,  0.0461,  0.0050},
                {0.0022,  0.0028,  0.0070,  0.0050,  0.0573}};
        //resust = MVO.efficientFrontier(ExpReturn,ExpCovariance,10);
        return resust;
    }
}
