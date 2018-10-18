package com.neo.web;

import com.neo.entity.TestEntity;
import com.neo.entity.YmlFileEntity;
import com.neo.returnType.*;
import com.neo.secvice.FundGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/27.
 */
@RestController
public class FundGroupController {

    @Autowired
    FundGroupService fundGroupService;

    @Autowired
    private YmlFileEntity ymlFileEntity;
    /**
     * 查询所有基金组合
     * @return
     */
    @ApiOperation("返回所有基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundAllReturn selectAllFundGroup(){
        FundAllReturn far = fundGroupService.selectAllFundGroup();
        return far;
    }
    @ApiOperation("返回所有基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/returnString", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String returnString(){
        System.out.println(ymlFileEntity.getSimpleProp());
        System.out.println(ymlFileEntity.getArrayProps());
        System.out.println(ymlFileEntity.getListProp1());
        System.out.println(ymlFileEntity.getListProp2());
        System.out.println(ymlFileEntity.getMapProps());
        return ymlFileEntity.getSimpleProp();
    }

    /**
     * 按照ID查询基金组合明细
     * @param id
     * @return
     */
    @ApiOperation("返回单个基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn selectById(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId){
        FundReturn fr= fundGroupService.selectById(id,subGroupId);
        return fr;
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param id
     * @param returntype
     * @param subGroupId
     * @return
     */
    @ApiOperation("预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Object> selectReturnAndPullback(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId,String returntype){
        Map<String,Object> map;
        map= fundGroupService.selectReturnAndPullback(id,returntype,subGroupId);
        return map;
    }

    /**
     * 配置收益贡献
     * @return
     */
    @ApiOperation("配置收益贡献")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/contributions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getRevenueContribution(@PathVariable("groupId") String uuid,@PathVariable("subGroupId") String subGroupId){
        RevenueContributionReturn rcb = fundGroupService.getRevenueContribution(uuid,subGroupId);
        return rcb;
    }

    /**
     * 有效前沿线
     * @return
     */
    @ApiOperation("有效前沿线")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn efficientFrontier(@PathVariable("groupId") String id){
        RevenueContributionReturn aReturn = fundGroupService.efficientFrontier(id);
        return aReturn;
    }

    /**
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     * @param id
     * @param subGroupId
     * @return
     */
    @ApiOperation("预期收益率调整 风险率调整  最优组合(有效前沿线)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/optimizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn getinterval(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId){
        FundReturn fr = fundGroupService.getinterval(id,subGroupId);
        return fr;
    }

    /**
     * 风险控制
     * @param id
     * @return
     */
    @ApiOperation("风险控制")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/risk-controls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getRiskController(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId){
        RevenueContributionReturn rct = fundGroupService.getRiskController(id,subGroupId);
        return rct;
    }

    /**
     * 风险控制手段与通知
     * @param uuid
     * @return
     */
    @ApiOperation("风险控制手段与通知")
    @RequestMapping(value = "/api/asset-allocation/products/{uuid}/risk-notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getmeansAndNoticesRetrun(@PathVariable("uuid") String uuid){
        //Return man = fundGroupService.getmeansAndNoticesRetrun();
        //return man;
        return "1213";
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     * @param uuid
     * @param riskLevel
     * @param investmentPeriod
     * @return
     */
    @ApiOperation("模拟历史年化业绩与模拟历史年化波动率")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PerformanceVolatilityReturn getPerformanceVolatility(@PathVariable("groupId") String uuid, String riskLevel, String investmentPeriod) {
        PerformanceVolatilityReturn riskIncomeIntervals= fundGroupService.getPerformanceVolatility(uuid,riskLevel,investmentPeriod);
        return riskIncomeIntervals;
    }

    /**
     * 分段数据
     * @param id
     * @param slidebarType（risk风险率     income收益率）
     * @return
     */
    @ApiOperation("滑动条分段数据")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getScaleMark(@PathVariable("groupId") String id,String slidebarType){
        RevenueContributionReturn smk = fundGroupService.getScaleMark(id,slidebarType);
        return smk;
    }

    /*@RequestMapping("/getFundGroupIncome/{id}")
    public double getFundGroupIncome(@PathVariable("id") String id,String starttime,String endtime){
        double income = fundGroupService.getFundGroupIncome(id,starttime,endtime);
        return income;
    }*/
    @RequestMapping(value = "/getList/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TestEntity> getFundGroupIncome(@PathVariable("id") int id, ServletResponse servletResponse){
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        List<TestEntity> aa = new ArrayList<>();
        if (id == 1){
            for (int i = 1;i<5;i++){
                TestEntity te = new TestEntity();
                te.setName("邮银财富·瑞享2017第106期");
                te.setBuyMoney("60,000.00");
                te.setBreathDay("2018/01/08");
                te.setExpectedAnnualizedRateOfReturn("4.95");
                te.setInvestmentHorizon("142天");
                te.setRiskRating("中低");
                te.setType("人民币");
                te.setCapitalPreservation("否");
                te.setIndex("position"+i);
                te.setCode("7268692"+i);
                aa.add(te);
            }
        }else if(id == 2){
            for (int i = 4;i<7;i++){
                TestEntity te = new TestEntity();
                te.setName("邮银财富·瑞享2017第106期");
                te.setBuyMoney("60,000.00");
                te.setBreathDay("2018/01/08");
                te.setExpectedAnnualizedRateOfReturn("4.95");
                te.setInvestmentHorizon("142天");
                te.setRiskRating("中低");
                te.setType("人民币");
                te.setCapitalPreservation("否");
                te.setIndex("entrust"+i);
                te.setCode("7268692"+i);
                aa.add(te);
            }
        }else if(id == 3){
            for (int i = 1;i<9;i++){
                TestEntity te = new TestEntity();
                te.setName("邮银财富·瑞享2017第106期");
                te.setBuyMoney("60,000.00");
                te.setBreathDay("2018/01/08");
                te.setExpectedAnnualizedRateOfReturn("4.95");
                te.setInvestmentHorizon("142天");
                te.setRiskRating("中低");
                te.setType("人民币");
                te.setCapitalPreservation("否");
                te.setIndex("trading"+i);
                te.setCode("7268692"+i);
                aa.add(te);
            }
        }
        return aa;
    }
}
