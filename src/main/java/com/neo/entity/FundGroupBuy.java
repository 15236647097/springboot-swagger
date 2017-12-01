package com.neo.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/12/1.
 */
public class FundGroupBuy {
    private String fund_group_name;//
    private Date buy_time;//
    private double buy_num;//
    private double proportion;//
    private String fund_id;//
    private double unitnav;//

    public String getFund_group_name() {
        return fund_group_name;
    }

    public void setFund_group_name(String fund_group_name) {
        this.fund_group_name = fund_group_name;
    }

    public Date getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(Date buy_time) {
        this.buy_time = buy_time;
    }

    public double getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(double buy_num) {
        this.buy_num = buy_num;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public String getFund_id() {
        return fund_id;
    }

    public void setFund_id(String fund_id) {
        this.fund_id = fund_id;
    }

    public double getUnitnav() {
        return unitnav;
    }

    public void setUnitnav(double unitnav) {
        this.unitnav = unitnav;
    }
}
