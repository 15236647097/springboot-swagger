package com.neo.entity;

import java.util.Date;

/**
 * Created by wangyinuo on 2017/12/1.
 */
public class FundNetValue {
    private double navadj;//
    private Date navlatestdate;//

    public double getNavadj() {
        return navadj;
    }

    public void setNavadj(double navadj) {
        this.navadj = navadj;
    }

    public Date getNavlatestdate() {
        return navlatestdate;
    }

    public void setNavlatestdate(Date navlatestdate) {
        this.navlatestdate = navlatestdate;
    }
}
