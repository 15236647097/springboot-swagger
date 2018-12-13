package com.neo;

import com.neo.entity.YmlFileEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;

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

    @Test
    public void calculateAdjustedFactor(){
        System.out.println(ymlFileEntity.getSimpleProp());

        logger.info("测试开始了");
        List<String> names = new ArrayList<>();

        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");
        names.forEach(System.out::println);
        try {
            System.out.println("HostName"+InetAddress.getLocalHost().getHostName());
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
            System.out.println(InetAddress.getLocalHost().getAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void java8Lambda(){
//        Arrays.asList( "a", "b", "d" ).forEach(
//                        ( e1, e2 ) -> e1.compareTo( e2 ));
//                e ->System.out.println( e )
//        Arrays.asList( "b", "a", "d" ).sort( ( e1, e2 ) -> e1.compareTo( e2 ) );
        Arrays.asList( "b", "a", "d" ).sort( comparing(String::toString) );
        String[] a = new String[]{"b", "a", "d"};
        List<String> b = Arrays.asList(a);
        b.sort(( e1, e2 ) -> {
            System.out.println(e1+e2);
            return e1.compareTo( e2 );
        } );
        for (String c : b){
            System.out.print(c);
        }
    }

    @Test
    public void java8LocalDate(){
        final LocalDate date = LocalDate.now();
        System.out.println("当前日期：" + date );
        System.out.println("获取当前日期的年份：" + date.getYear() );
        System.out.println("获取当前日期是第几月：" + date.getMonthValue() );
        System.out.println("表示该对象表示的日期是这个月第几天：" + date.getDayOfMonth() );
        System.out.println("表示该对象表示的日期是星期几：" + date.getDayOfWeek().getValue() );
        System.out.println("表示该对象表示的日期是今年第几天" + date.getDayOfYear() );
        System.out.println("是否是闰年：" + date.isLeapYear());

        final LocalTime time = LocalTime.now();
        System.out.println("当前时间：" + time );
        System.out.println("纳秒值：" + time.getNano() );
        System.out.println("小时：" + time.getHour() );
        System.out.println("分钟：" + time.getMinute() );
        System.out.println("秒：" + time.getSecond() );
        System.out.println("秒：" + time.getLong(ChronoField.CLOCK_HOUR_OF_DAY) );
    }

}