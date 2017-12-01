package com.neo.secvice;

import com.neo.entity.*;
import com.neo.returnType.*;
import com.neo.mapper.FundGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.CalculatePriceAndYieldService;
import util.MVO;

import java.util.*;

/**
 * Created by wangyinuo on 2017/11/27.
 */
@Service
public class FundGroupService {
    @Autowired
    private FundGroupMapper fundGroupMapper;

    /**
     * 查询所有基金组合
     * @return
     */
    public FundAllReturn selectAllFundGroup(){
        List<Interval> fundGroup = fundGroupMapper.selectAllFundGroup();
        List<Interval> fundGroupNum = fundGroupMapper.selectAllFundGroupNum();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        FundAllReturn far = new FundAllReturn();

        for(int i = 0;i<fundGroupNum.size();i++){
            Map<String,Object> _items = new HashMap<String,Object>();
            Map<String,String> _links = new HashMap<String,String>();
            Map<String,Double> assetsRatios = new HashMap<String,Double>();
            for(Interval interval:fundGroup){
                if(interval.getFund_group_id().equalsIgnoreCase(fundGroupNum.get(i).getFund_group_id())){
                    List<Interval> intervals = fundGroupMapper.getProportion(interval.getFund_group_id());
                    //基金组合内的各基金权重
                    for (Interval inter: intervals){
                        assetsRatios.put(inter.getFund_income_type(),inter.getProportion());
                    }
                    _items.put("groupId",interval.getFund_group_id());
                    _items.put("subGroupId",interval.getRisk_income_interval_id());
                    _items.put("name",interval.getFund_group_name());
                }
                _items.put("minAnnualizedReturn",interval.getIncome_min_num());
                _items.put("maxAnnualizedReturn",interval.getIncome_max_num());
                _items.put("minRiskLevel",interval.getRisk_min_num());
                _items.put("maxRiskLevel",interval.getRisk_max_num());
                _items.put("creationTime",interval.getDetails_last_mod_time().getTime());//时间戳
                _items.put("assetsRatios",assetsRatios);//组合内各基金权重
                far.set_links(_links);
            }
            list.add(_items);
        }
        far.setName("基金组合");
        far.set_total(fundGroupNum.size());
        far.set_items(list);
        far.set_schemaVersion("0.1.1");
        far.set_serviceId("资产配置");
        return far;
    }

    /**
     * 按照ID查询基金组合明细
     * @param id
     * @return
     */
    public FundReturn selectById(String id,String subGroupId){
        FundReturn fr = null;
        List<Interval> interval = fundGroupMapper.selectById(id,subGroupId);
        if(interval.size() != 0) {
            fr = getFundReturn(interval);
        }
        return fr;
    }

    /**
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     * @param id
     * @param subGroupId
     * @return
     */
    public FundReturn getinterval(String id,String subGroupId){
        FundReturn fr = null;
        Map<String, Object> map = new HashMap<>();
        map.put("subGroupId", subGroupId);
        map.put("id", id);
        List<Interval> interval = fundGroupMapper.getinterval(map);
        if(interval.size() != 0) {
            fr = getFundReturn(interval);
        }
        return fr;
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param id
     * @param returntype
     * @param subGroupId
     * @return
     */
    public Map<String,Object> selectReturnAndPullback(String id, String returntype,String subGroupId){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("subGroupId",subGroupId);
        Interval interval = fundGroupMapper.selectReturnAndPullback(map);
        map.clear();
        if(returntype.equalsIgnoreCase("1")) {
            map.put("name", "预期年化收益");
            map.put("value", interval.getExpected_annualized_return());
        }else {
            map.put("name", "预期最大回撤");
            map.put("value", interval.getExpected_max_retracement());
        }
        return map;
    }

    /**
     * 配置收益贡献
     * @return
     */
    public RevenueContributionReturn getRevenueContribution(String id,String subGroupId){
        RevenueContributionReturn rcb = new RevenueContributionReturn();
        Map<String,String> _links = new HashMap<String,String>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<>();
        map.put("subGroupId", subGroupId);
        map.put("id", id);
        List<Interval> itr = fundGroupMapper.getRevenueContribution(map);
        for(int i = 0;i<itr.size();i++){
            Map<String,Object> _items = new HashMap<String,Object>();
            _items.put("id",i);
            _items.put("name",itr.get(i).getFund_income_type());
            _items.put("value",itr.get(i).getRevenue_contribution());
            list.add(_items);
        }
        rcb.setName("配置收益贡献");
        rcb.set_total(itr.size());
        rcb.set_items(list);
        rcb.set_links(_links);
        rcb.set_schemaVersion("0.1.1");
        rcb.set_serviceId("资产配置");
        return rcb;
    }

    /**
     * 有效前沿线
     * @return
     */
    public RevenueContributionReturn efficientFrontier(){
        Map<String,Object> map = new HashMap<String,Object>();
        RevenueContributionReturn aReturn = new RevenueContributionReturn();
        Map<String,String> _links = new HashMap<String,String>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        List<float [][]> resust = null;
        Double [] ExpReturn = { 0.0054, 0.0531, 0.0779, 0.0934, 0.0130 };
        Double[][] ExpCovariance = {{0.0569,  0.0092,  0.0039,  0.0070,  0.0022},
                {0.0092,  0.0380,  0.0035,  0.0197,  0.0028},
                {0.0039,  0.0035,  0.0997,  0.0100,  0.0070},
                {0.0070,  0.0197,  0.0100,  0.0461,  0.0050},
                {0.0022,  0.0028,  0.0070,  0.0050,  0.0573}};
        resust = MVO.efficientFrontier(ExpReturn,ExpCovariance,10);
        for (int i = 0;i<10;i++){
            Map<String,Object> _items = new HashMap<String,Object>();
            _items.put("id",1);
            _items.put("x",resust.get(0)[i][0]);
            _items.put("y",resust.get(1)[i][0]);
                List<Float> list1 = new ArrayList<Float>();
                list1.add(resust.get(2)[0][i]);
                list1.add(resust.get(2)[1][i]);
                list1.add(resust.get(2)[2][i]);
                list1.add(resust.get(2)[3][i]);
                list1.add(resust.get(2)[4][i]);
                _items.put("w",list1);

            list.add(_items);
        }
        aReturn.setName("有效前沿线数据");
        aReturn.set_items(list);
        aReturn.set_total(10);
        aReturn.set_links(_links);
        aReturn.set_schemaVersion("0.1.1");
        aReturn.set_serviceId("资产配置");
        return aReturn;
    }

    /**
     * 风险控制
     * @param id
     * @return
     */
    public RevenueContributionReturn getRiskController(String id,String subGroupId){
        List<RiskController> riskControllers = fundGroupMapper.getRiskController(id,subGroupId);
        RevenueContributionReturn rct = new RevenueContributionReturn();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,String> _links = new HashMap<String,String>();
        rct.set_total(riskControllers.size());
        for (RiskController riskController:riskControllers){
            Map<String,Object> _items = new HashMap<String,Object>();
            _items.put("id",riskController.getId());
            _items.put("name",riskController.getName());
            _items.put("level2RiskControl",riskController.getRisk_controller());
            _items.put("benchmark",riskController.getBenchmark());
            list.add(_items);
        }
        rct.setName("风险控制");
        rct.set_items(list);
        rct.set_links(_links);
        rct.set_schemaVersion("0.1.1");
        rct.set_serviceId("资产配置");
        return rct;
    }

    /**
     * 风险控制手段与通知
     * @return
     */
    public Return getmeansAndNoticesRetrun(){
        Return man = new Return();
        Map<String,String> _links = new HashMap<String,String>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        man.set_total(4);
        for (int i = 0;i<4;i++){
            Map<String,Object> _items = new HashMap<String,Object>();
            if (i==0){
                _items.put("id",1);
                _items.put("name","全市场的系统风险");
                _items.put("content",null);
            }else if (i == 1){
                _items.put("id",2);
                _items.put("name","各类资产的市场风险");
                _items.put("content",null);
            }else if (i == 2){
                _items.put("id",3);
                _items.put("name","风险控制是第一要位!作任何的投资，防范风险是关键的");
                _items.put("content",null);
            }else if (i == 3){
                _items.put("id",4);
                _items.put("name","具备相关钩子的专业知识");
                _items.put("content",null);
            }
            list.add(_items);
        }
        man.set_items(list);
        man.set_links(_links);
        man.set_schemaVersion("0.1.1");
        man.set_serviceId("资产配置");
        return man;
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     * @param id
     * @param cust_risk
     * @param investment_horizon
     * @return
     */
    public PerformanceVolatilityReturn getPerformanceVolatility(String id,String cust_risk,String investment_horizon) {
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,String> _links = new HashMap<String,String>();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        PerformanceVolatilityReturn aReturn = new PerformanceVolatilityReturn();
        map.put("id",id);
        map.put("cust_risk",cust_risk);
        map.put("investment_horizon",investment_horizon);
        List<RiskIncomeInterval> riskIncomeIntervals= fundGroupMapper.getPerformanceVolatility(map);
        RiskIncomeInterval riskIncomeInterval = riskIncomeIntervals.get(riskIncomeIntervals.size()/2);
        aReturn.setName("模拟数据");
        aReturn.setProductGroupId(riskIncomeInterval.getFund_group_id());
        aReturn.setProductSubGroupId(riskIncomeInterval.getId());
        for (int i = 0; i < 4; i++){
            Map<String,Object> maps = new HashMap<String,Object>();
            if (i==0){
                maps.put("id",1);
                maps.put("name","模拟历史年化业绩");
                maps.put("value",riskIncomeInterval.getSimulate_historical_year_performance());
            }else if (i == 1){
                maps.put("id",2);
                maps.put("name","模拟历史年化波动率");
                maps.put("value",riskIncomeInterval.getSimulate_historical_volatility());
            }else if (i == 2){
                maps.put("id",3);
                maps.put("name","置信区间");
                maps.put("value",riskIncomeInterval.getConfidence_interval());
            }else if (i == 3){
                maps.put("id",4);
                maps.put("name","最大亏损额");
                maps.put("value",riskIncomeInterval.getMaximum_losses());
            }
            list.add(maps);
        }
        aReturn.set_items(list);
        aReturn.set_links(_links);
        aReturn.set_schemaVersion("0.1.1");
        aReturn.set_serviceId("资产配置");
        return aReturn;
    }

    public RevenueContributionReturn getScaleMark(String id,String slidebarType){
        RevenueContributionReturn smk = new RevenueContributionReturn();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,String> _links = new HashMap<String,String>();
        List<RiskIncomeInterval> riskIncomeIntervalList = fundGroupMapper.getScaleMark(id);
        if (slidebarType.equalsIgnoreCase("risk")) {
            smk.setName("风险率");
            for (int i = 0; i<riskIncomeIntervalList.size();i++){
                Map<String,Object> maps = new HashMap<String,Object>();
                maps.put("id",i+1);
                maps.put("value",riskIncomeIntervalList.get(i).getRisk_num());
                list.add(maps);
            }
        }else {
            smk.setName("收益率");
            for (int i = 0; i<riskIncomeIntervalList.size();i++){
                Map<String,Object> maps = new HashMap<String,Object>();
                maps.put("id",i+1);
                maps.put("value",riskIncomeIntervalList.get(i).getIncome_num());
                list.add(maps);
            }
        }
        smk.set_items(list);
        smk.set_total(riskIncomeIntervalList.size());
        smk.set_links(_links);
        smk.set_schemaVersion("0.1.1");
        smk.set_serviceId("资产配置");
        return smk;
    }

    public double getFundGroupIncome(String id,String starttime,String endtime){
        CalculatePriceAndYieldService cpas = new CalculatePriceAndYieldService();
        List<FundGroupBuy> list = fundGroupMapper.getFundGroupBuy(id);
        double num = 0;
        for(FundGroupBuy fundGroupBuy : list){
            List<FundNetValue> fundNetValues = fundGroupMapper.getFundNetValue(fundGroupBuy.getFund_id(),starttime,endtime);
            List<Double> list1 = new ArrayList<Double>();
            list1.add(fundNetValues.get(0).getNavadj());
            list1.add(fundNetValues.get(fundNetValues.size()-1).getNavadj());
            list1 = cpas.calculatePriceToYield(list1,"Simple");
            num+=list1.get(0)*fundGroupBuy.getProportion();
        }
        return num;
    }

    /**
     * 把传出数据转为json格式
     * @param interval
     * @return
     */
    public FundReturn getFundReturn(List<Interval> interval){
        FundReturn fr = new FundReturn();
        if (interval.size()!=0) {
            Map<String, Double> assetsRatios = new HashMap<String, Double>();
            Map<String, String> _links = new HashMap<String, String>();
            List<Map<String, Double>> list = new ArrayList<Map<String, Double>>();
            List<Interval> intervals = fundGroupMapper.getProportion(interval.get(0).getFund_group_id());
            //基金组合内的各基金权重
            for (Interval inter : intervals) {
                assetsRatios.put(inter.getFund_income_type(), inter.getProportion());
            }
            list.add(assetsRatios);
            fr.setGroupId(interval.get(0).getFund_group_id());
            fr.setSubGroupId(interval.get(0).getId());
            fr.setName(interval.get(0).getFund_group_name());
            fr.setMinAnnualizedReturn(interval.get(0).getIncome_min_num());
            fr.setMaxAnnualizedReturn(interval.get(0).getIncome_max_num());
            fr.setMinRiskLevel(interval.get(0).getRisk_min_num());
            fr.setMaxRiskLevel(interval.get(0).getRisk_max_num());
            fr.set_links(_links);
            fr.setCreationTime(interval.get(0).getDetails_last_mod_time().getTime());
            fr.set_schemaVersion("0.1.1");
            fr.set_serviceId("资产配置");
            fr.setAssetsRatios(list);
        }
        return fr;
    }


    /**    不一定用
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param id
     * @param selecttype
     * @param returntype
     * @param num
     * @return
     */
    public Map<String,Object> daidingselectReturnAndPullback(String id, String selecttype, String returntype, float num){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("type",selecttype);
        map.put("num",num);
        Interval interval = fundGroupMapper.selectReturnAndPullback(map);
        map.clear();
        if(returntype.equalsIgnoreCase("1")) {
            map.put("name", "预期年化收益");
            map.put("value", interval.getExpected_annualized_return());
        }else {
            map.put("name", "预期最大回撤");
            map.put("value", interval.getExpected_max_retracement());
        }
        return map;
    }
}