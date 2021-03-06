package com.neo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/25
 * Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class TestJava {


    @Test
    public void testIntern() {
        String s1 = "ab";
        String s2 = "a" + "b";
        String s3 = "a";
        String s4 = "b";
        String s5 = s3 + s4;
        String s6 = "ab";
        System.out.println(s1.getBytes());
        System.out.println(s6.getBytes());
        System.out.println(s2.getBytes());
        System.out.println(s5.getBytes());
        System.out.println(s5 == s2);
    }

    @Test
    public void testIntern1() {
        String s1 = "ab123";
        String s2 = new String("ab123");
        System.out.println(s1 == s2);
        String s3 = s2.intern();
        System.out.println(s1 == s3);
    }

    @Test
    public void testSubString() {
        String a = "0123";
        System.out.println(a.substring(0, 2));
    }

    @Test
    public void testSwitch() {
        String a = "5";
        switch (a) {
            case "0":
                System.out.println(0);
            case "1":
                System.out.println(1);
            case "2":
                System.out.println(2);
            case "3":
                System.out.println(3);
        }
    }

    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID().toString());
    }

    @Test
    public void testCompareTo() {
        String a = "2.0.0";
        String b = "2.0.0";
        String c = "2.0.1";
        String d = "1.0.1";
        String e = "1.0";
        String f = "2";
        System.out.println(a.compareToIgnoreCase(b));
        System.out.println(a.compareToIgnoreCase(c));
        System.out.println(a.compareToIgnoreCase(d));
        System.out.println(a.compareToIgnoreCase(e));
        System.out.println(a.compareToIgnoreCase(f));
    }

    @Test
    public void testMatches() {
        String s = "v1.2.3";
        if (!s.startsWith("\\D")) { //这样也可以判断
//        if (! s.matches("^\\d")) { //这样也可以判断
            s = s.replaceAll("^\\D*", "");
        }
        System.out.println(s);
    }

}