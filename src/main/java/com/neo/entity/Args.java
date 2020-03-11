package com.neo.entity;

/**
 * @Auther: ynwang
 * @Date: 2020/1/20
 * @Description:
 */
public class Args {
    private String aa;

    private String aa1;

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public String getAa1() {
        return aa1;
    }

    public void setAa1(String aa1) {
        this.aa1 = aa1;
    }

    public static void main(String[] args) {
        aa();
    }
    private static void aa(){

        System.out.println(String.valueOf(""));
        System.out.println(String.valueOf(""));

        System.out.println(null + "main");
        if (true) return;
        try{
            System.out.println("try");
        }catch (Exception e){
            System.out.println("e");
        }finally {
            System.out.println("finally");

        }
    }
}
