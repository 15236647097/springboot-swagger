package com.neo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import util.MVO;

/**
 * Created by webrx on 2017/12/14 0014.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SharpeRatio {
    @Test
    public void sharpeRatio(){
        Double[] asset ={1.2000,   1.3000  ,  0.9000 ,   1.5000};
        Double cash = 0.0135;
        MVO.sharpeRatio(asset,cash);
    }
}
