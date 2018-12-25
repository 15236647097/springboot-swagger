package com.neo.entity;

import java.util.List;

/**
 * Created by wyn on 2018/12/25.
 */
public class FundGroup1 {
    private String id;
    private String fund_group_name;
    private String status;
    private String start_time;
    private String stop_time;
    private String risk_min_num;
    private String risk_max_num;
    private String income_min_num;
    private String income_max_num;
    private String group_add_time;
    private String group_last_mod_time;
    private String base_line_id;
    private List<FundGroupDetails1> fundGroupDetails1s;

    public List<FundGroupDetails1> getFundGroupDetails1s() {
        return fundGroupDetails1s;
    }

    public void setFundGroupDetails1s(List<FundGroupDetails1> fundGroupDetails1s) {
        this.fundGroupDetails1s = fundGroupDetails1s;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFund_group_name() {
        return fund_group_name;
    }

    public void setFund_group_name(String fund_group_name) {
        this.fund_group_name = fund_group_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    public String getRisk_min_num() {
        return risk_min_num;
    }

    public void setRisk_min_num(String risk_min_num) {
        this.risk_min_num = risk_min_num;
    }

    public String getRisk_max_num() {
        return risk_max_num;
    }

    public void setRisk_max_num(String risk_max_num) {
        this.risk_max_num = risk_max_num;
    }

    public String getIncome_min_num() {
        return income_min_num;
    }

    public void setIncome_min_num(String income_min_num) {
        this.income_min_num = income_min_num;
    }

    public String getIncome_max_num() {
        return income_max_num;
    }

    public void setIncome_max_num(String income_max_num) {
        this.income_max_num = income_max_num;
    }

    public String getGroup_add_time() {
        return group_add_time;
    }

    public void setGroup_add_time(String group_add_time) {
        this.group_add_time = group_add_time;
    }

    public String getGroup_last_mod_time() {
        return group_last_mod_time;
    }

    public void setGroup_last_mod_time(String group_last_mod_time) {
        this.group_last_mod_time = group_last_mod_time;
    }

    public String getBase_line_id() {
        return base_line_id;
    }

    public void setBase_line_id(String base_line_id) {
        this.base_line_id = base_line_id;
    }
}
