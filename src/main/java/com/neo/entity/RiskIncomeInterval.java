package com.neo.entity;

/**
 * Created by wangyinuo on 2017/11/28.
 */
public class RiskIncomeInterval extends Strategy {
    private double risk_num;//风险率
    private double income_num;//收益率
    private double simulate_historical_volatility;//模拟历史年化波动率
    private double simulate_historical_year_performance;//模拟历史年化业绩
    private double confidence_interval;//置信区间
    private double maximum_losses;//最大亏损额

    public double getRisk_num() {
        return risk_num;
    }

    public void setRisk_num(double risk_num) {
        this.risk_num = risk_num;
    }

    public double getIncome_num() {
        return income_num;
    }

    public void setIncome_num(double income_num) {
        this.income_num = income_num;
    }

    public double getConfidence_interval() {
        return confidence_interval;
    }

    public void setConfidence_interval(double confidence_interval) {
        this.confidence_interval = confidence_interval;
    }

    public double getMaximum_losses() {
        return maximum_losses;
    }

    public void setMaximum_losses(double maximum_losses) {
        this.maximum_losses = maximum_losses;
    }

    public double getSimulate_historical_volatility() {
        return simulate_historical_volatility;
    }

    public void setSimulate_historical_volatility(double simulate_historical_volatility) {
        this.simulate_historical_volatility = simulate_historical_volatility;
    }

    public double getSimulate_historical_year_performance() {
        return simulate_historical_year_performance;
    }

    public void setSimulate_historical_year_performance(double simulate_historical_year_performance) {
        this.simulate_historical_year_performance = simulate_historical_year_performance;
    }
}
