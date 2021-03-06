package com.neo.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public class FundGroupDetails extends FundGroup{
    private String fund_code;//基金ID
    private String fund_group_id;//基金組合ID
    private double proportion;//权重
    private double revenue_contribution;//配置收益贡献
    private String risk_income_interval_id;//动态调仓区间id
    private Date details_last_mod_time;//最后一次修改时间
    private List<FundGroupDetails> fundGroupDetailsList;

    public String getRisk_income_interval_id() {
        return risk_income_interval_id;
    }

    public void setRisk_income_interval_id(String risk_income_interval_id) {
        this.risk_income_interval_id = risk_income_interval_id;
    }

    public Date getDetails_last_mod_time() {
        return details_last_mod_time;
    }

    public void setDetails_last_mod_time(Date details_last_mod_time) {
        this.details_last_mod_time = details_last_mod_time;
    }

    public double getRevenue_contribution() {
        return revenue_contribution;
    }

    public void setRevenue_contribution(double revenue_contribution) {
        this.revenue_contribution = revenue_contribution;
    }

    public String getFund_id() {
        return fund_code;
    }

    public void setFund_id(String fund_code) {
        this.fund_code = fund_code;
    }

    public List<FundGroupDetails> getFundGroupDetailsList() {
        return fundGroupDetailsList;
    }

    public void setFundGroupDetailsList(List<FundGroupDetails> fundGroupDetailsList) {
        this.fundGroupDetailsList = fundGroupDetailsList;
    }

    public String getFund_group_id() {
        return fund_group_id;
    }

    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

}
