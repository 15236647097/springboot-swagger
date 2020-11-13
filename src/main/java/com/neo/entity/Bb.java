package com.neo.entity;

import java.util.Map;

/**
 * @Auther: ynwang
 * @Date: 2020/1/20
 * @Description:
 */
public class Bb {

    private String ciphertext;

    private Map<String,Object> args;

    public String getBb() {
        return ciphertext;
    }

    public void setBb(String bb) {
        this.ciphertext = bb;
    }

    public Map<String,Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String,Object> args) {
        this.args = args;
    }
}
