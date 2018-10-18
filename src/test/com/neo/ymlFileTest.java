package com.neo;

import com.neo.entity.YmlFileEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/25
 * Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ymlFileTest {

    @Autowired
    private YmlFileEntity ymlFileEntity;

    private static final Logger logger= LoggerFactory.getLogger(ymlFileTest.class);

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void calculateAdjustedFactor(){
        System.out.println(ymlFileEntity.getSimpleProp());
    }

}