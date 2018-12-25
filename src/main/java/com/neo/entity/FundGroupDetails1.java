package com.neo.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public class FundGroupDetails1 extends FundGroup{
    private String id;
    private String fund_group_id;
    private String risk_num;
    private String income_num;
    private String expected_annualized_return;
    private String expected_max_retracement;
    private String simulate_historical_volatility;
    private String simulate_historical_year_performance;
    private String confidence_interval;
    private String maximum_losses;
    private String sharpe_ratio;
    private String create_time;
    private String interval_last_mod_time;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getFund_group_id() {
        return fund_group_id;
    }

    @Override
    public void setFund_group_id(String fund_group_id) {
        this.fund_group_id = fund_group_id;
    }

    public String getRisk_num() {
        return risk_num;
    }

    public void setRisk_num(String risk_num) {
        this.risk_num = risk_num;
    }

    public String getIncome_num() {
        return income_num;
    }

    public void setIncome_num(String income_num) {
        this.income_num = income_num;
    }

    public String getExpected_annualized_return() {
        return expected_annualized_return;
    }

    public void setExpected_annualized_return(String expected_annualized_return) {
        this.expected_annualized_return = expected_annualized_return;
    }

    public String getExpected_max_retracement() {
        return expected_max_retracement;
    }

    public void setExpected_max_retracement(String expected_max_retracement) {
        this.expected_max_retracement = expected_max_retracement;
    }

    public String getSimulate_historical_volatility() {
        return simulate_historical_volatility;
    }

    public void setSimulate_historical_volatility(String simulate_historical_volatility) {
        this.simulate_historical_volatility = simulate_historical_volatility;
    }

    public String getSimulate_historical_year_performance() {
        return simulate_historical_year_performance;
    }

    public void setSimulate_historical_year_performance(String simulate_historical_year_performance) {
        this.simulate_historical_year_performance = simulate_historical_year_performance;
    }

    public String getConfidence_interval() {
        return confidence_interval;
    }

    public void setConfidence_interval(String confidence_interval) {
        this.confidence_interval = confidence_interval;
    }

    public String getMaximum_losses() {
        return maximum_losses;
    }

    public void setMaximum_losses(String maximum_losses) {
        this.maximum_losses = maximum_losses;
    }

    public String getSharpe_ratio() {
        return sharpe_ratio;
    }

    public void setSharpe_ratio(String sharpe_ratio) {
        this.sharpe_ratio = sharpe_ratio;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getInterval_last_mod_time() {
        return interval_last_mod_time;
    }

    public void setInterval_last_mod_time(String interval_last_mod_time) {
        this.interval_last_mod_time = interval_last_mod_time;
    }
}
