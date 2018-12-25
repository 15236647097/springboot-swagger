package com.neo;

import com.neo.secvice.FundGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/25
 * Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class FundGroupServiceTest {

    @Autowired
    FundGroupService fundGroupService;

    @Test
    public void java8LocalDate(){
        fundGroupService.getFundList("7065");
    }

}