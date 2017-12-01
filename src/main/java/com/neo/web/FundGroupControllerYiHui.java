package com.neo.web;

import com.neo.entity.*;
import com.neo.mapper.FundGroupMapper;
import com.neo.returnType.FundReturn;
import com.neo.returnType.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyinuo on 2017/11/16.
 */

@RestController()
public class FundGroupControllerYiHui {

    @Autowired
    private FundGroupMapper fundGroupMapper;

    /**
     * 传客户ID和风险值查询基金组合
     * @param id
     * @param risk
     * @param incomeyear
     * @return
     */
    @RequestMapping("/yihui/getFundGroup1/{id}/{risk}/{incomeyear}")
    public FundReturn selectById1(@PathVariable("id") String id, @PathVariable("risk") String risk, @PathVariable("incomeyear") String incomeyear){
        Map<String, String> map = new HashMap<>();
        map.put("risk", risk);
        map.put("incomeyear", incomeyear);
        List<Interval> interval = fundGroupMapper.selectFundGroup(map);
        if(interval != null) {
            String fund_group_id = interval.get(0).getFund_group_id();
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            map.put("custID", id);
            map.put("fundGroupID", fund_group_id);
            map.put("time", dateString);
            fundGroupMapper.insertRecommendHistory(map);
        }
        FundReturn fr = getFundReturn(interval);
        return fr;
    }

    /**
     * 按照ID查询基金组合明细
     * @param id
     * @return
     */
    @RequestMapping("/yihui/api/asset-allocation/products/{id}/adjustment/ssub-groups/{subGroupId}")
    public FundReturn selectById(@PathVariable("id") String id,@PathVariable("subGroupId") String subGroupId){
        List<Interval> interval = fundGroupMapper.selectById(id,subGroupId);
        FundReturn fr = getFundReturn(interval);
        return fr;
    }

    /**
     * 查询所有基金组合
     * @return
     */
    @RequestMapping("/yihui/getAllFundGroup")
    public Return selectAllFundGroup(){
        List<Interval> fundGroup = fundGroupMapper.selectAllFundGroup();
        List<Interval> fundGroupNum = fundGroupMapper.selectAllFundGroupNum();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Return far = new Return();

        for(int i = 0;i<fundGroupNum.size();i++){
            Map<String,Object> _items = new HashMap<String,Object>();
            Map<String,String> investmentPeriod = new HashMap<String,String>();
            Map<String,String> riskToleranceLevel = new HashMap<String,String>();
            Map<String,String> _links = new HashMap<String,String>();
            Map<String,Double> assetsRatios = new HashMap<String,Double>();
            for(Interval interval:fundGroup){
                if(interval.getFund_group_id().equalsIgnoreCase(fundGroupNum.get(i).getFund_group_id())){
                    //投资年限
                    if(interval.getInvestment_horizon().equalsIgnoreCase("1")){
                        investmentPeriod.put("name","短期");
                        investmentPeriod.put("value","0-1年");
                    }else if(interval.getInvestment_horizon().equalsIgnoreCase("2")){
                        investmentPeriod.put("name","中期");
                        investmentPeriod.put("value","1-3年");
                    }else if(interval.getInvestment_horizon().equalsIgnoreCase("3")){
                        investmentPeriod.put("name","长期");
                        investmentPeriod.put("value","3年以上");
                    }
                    if(interval.getCust_risk().equalsIgnoreCase("1")){
                        riskToleranceLevel.put("name","保守型");
                        riskToleranceLevel.put("value","C1");
                    }else if(interval.getCust_risk().equalsIgnoreCase("2")){
                        riskToleranceLevel.put("name","稳健型");
                        riskToleranceLevel.put("value","C2");
                    }else if(interval.getCust_risk().equalsIgnoreCase("3")){
                        riskToleranceLevel.put("name","平衡型");
                        riskToleranceLevel.put("value","C3");
                    }else if(interval.getCust_risk().equalsIgnoreCase("4")){
                        riskToleranceLevel.put("name","积极型");
                        riskToleranceLevel.put("value","C4");
                    }else if(interval.getCust_risk().equalsIgnoreCase("5")){
                        riskToleranceLevel.put("name","进取型");
                        riskToleranceLevel.put("value","C5");
                    }
                    _items.put("riskToleranceLevel",riskToleranceLevel);

                    assetsRatios.put(interval.getName(),interval.getProportion());
                    _items.put("productUuid",interval.getFund_group_id());
                    _items.put("productName",interval.getFund_group_name());
                }
                _items.put("riskToleranceLevel",riskToleranceLevel);//客户风险值
                _items.put("creationTime",new Date().getTime());//时间戳
                _items.put("assetsRatios",assetsRatios);//组合内各基金权重
                far.set_links(_links);
            }
            list.add(_items);
        }
        far.set_total(fundGroupNum.size());
        far.set_items(list);
        far.set_schemaVersion("0.1.1");
        far.set_serviceId("资产配置");
        return far;
    }

    /**
     * 根据ID修改基金组合的启用状态
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/yihui/updateStatus/{id}/{status}")
    public int updateStatus(@PathVariable("id") String id, @PathVariable("status") String status){
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        map.put("time", dateString);
        int num = fundGroupMapper.updateStatus(map);
        return num;
    }

    /**
     * 手动添加基金组合
     * @param name
     * @param fundGroupDetailslist
     * @return
     */
    @RequestMapping("/yihui/insertFundGroup")
    public int insertFundGroup(String name,FundGroupDetails fundGroupDetailslist){
        FundGroup fundGroup = new FundGroup();
        fundGroup.setFund_group_name(name);
        fundGroup.setGroup_add_time(new Date());
        fundGroupMapper.insertFundGroup(fundGroup);
        for (FundGroupDetails fundGroupDetails: fundGroupDetailslist.getFundGroupDetailsList()){
            fundGroupDetails.setFund_group_id(fundGroup.getId());
            fundGroupDetails.setDetails_last_mod_time(new Date());
        }
        int num = fundGroupMapper.insertFundGroupDetail(fundGroupDetailslist.getFundGroupDetailsList());
        return num;
    }

    /**
     * 根据风险值或收益率查询组合内基金权重
     * @param id
     * @param type
     * @param num
     * @return
     */
    @RequestMapping("/yihui/api/asset-allocation/products/{num}/{type}/{id}/optimizations")
    public FundReturn getinterval(@PathVariable("id") String id, @PathVariable("type") String type, @PathVariable("num") float num){
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("num", num);
        map.put("id", id);
        List<Interval> interval = fundGroupMapper.getinterval(map);
        FundReturn fr = getFundReturn(interval);
        return fr;
    }

    public FundReturn getFundReturn(List<Interval> interval){
        FundReturn fr = new FundReturn();
        Map<String,Double> assetsRatios = new HashMap<String,Double>();
        Map<String,String> investmentPeriod = new HashMap<String,String>();
        Map<String,String> riskToleranceLevel = new HashMap<String,String>();
        Map<String,String> _links = new HashMap<String,String>();
        List<Map<String,Double>> list = new ArrayList<Map<String,Double>>();
        //基金组合内的各基金权重
        for (Interval inter: interval){
            assetsRatios.put(inter.getName(),inter.getProportion());
        }
        list.add(assetsRatios);
        //投资年限
        if(interval.get(0).getInvestment_horizon().equalsIgnoreCase("1")){
            investmentPeriod.put("name","短期");
            investmentPeriod.put("value","0-1年");
        }else if(interval.get(0).getInvestment_horizon().equalsIgnoreCase("2")){
            investmentPeriod.put("name","中期");
            investmentPeriod.put("value","1-3年");
        }else if(interval.get(0).getInvestment_horizon().equalsIgnoreCase("3")){
            investmentPeriod.put("name","长期");
            investmentPeriod.put("value","3年以上");
        }
        //客户风险值
        if(interval.get(0).getCust_risk().equalsIgnoreCase("1")){
            riskToleranceLevel.put("name","保守型");
            riskToleranceLevel.put("value","C1");
        }else if(interval.get(0).getCust_risk().equalsIgnoreCase("2")){
            riskToleranceLevel.put("name","稳健型");
            riskToleranceLevel.put("value","C2");
        }else if(interval.get(0).getCust_risk().equalsIgnoreCase("3")){
            riskToleranceLevel.put("name","平衡型");
            riskToleranceLevel.put("value","C3");
        }else if(interval.get(0).getCust_risk().equalsIgnoreCase("4")){
            riskToleranceLevel.put("name","积极型");
            riskToleranceLevel.put("value","C4");
        }else if(interval.get(0).getCust_risk().equalsIgnoreCase("5")){
            riskToleranceLevel.put("name","进取型");
            riskToleranceLevel.put("value","C5");
        }

        fr.setInvestmentPeriod(investmentPeriod);
        fr.setRiskToleranceLevel(riskToleranceLevel);
        fr.set_links(_links);
        fr.setCreationTime(new Date().getTime());
        fr.set_schemaVersion("0.1.1");
        fr.set_serviceId("资产配置");
        fr.setAssetsRatios(list);
        return fr;
    }
}
