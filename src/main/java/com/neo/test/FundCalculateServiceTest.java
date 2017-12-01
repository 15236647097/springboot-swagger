package com.neo.test;

//import static org.junit.Assert.*;

import com.neo.entity.FundCalculateData;
import com.neo.entity.FundNetVal;
import com.neo.mapper.FundCalculateDataMapper;
import com.neo.mapper.FundNetValMapper;
import com.neo.job.entity.JobTimeRecord;
import com.neo.job.service.JobTimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static util.ConstantUtil.*;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/17
 * Desc:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)


public class FundCalculateServiceTest {
    @Autowired
    private FundNetValMapper fundNetValMapper;

    @Autowired
    private FundCalculateDataMapper fundCalculateDataMapper;

    @Autowired
    private JobTimeService jobTimeService;

    private static final Logger logger= LoggerFactory.getLogger(FundCalculateServiceTest.class);

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    private int countOfWeek=0;

    private Map<String,FundNetVal> monthMap=new HashMap<>();

    private Map<String,FundNetVal> yearMap=new HashMap<>();

    /*
     * 根据时间查询净值表中复权单位净值数据
     */
    public Map<String,List<FundNetVal>> selectFundNetValueByDate( Date selectDate){

        List<FundNetVal> fundNetValArrList=new ArrayList<>();
        try {
            fundNetValArrList =fundNetValMapper.getAllByDate(selectDate);
        }catch (Exception e){
            logger.error("查询净值数据失败!");
            e.printStackTrace();
        }

        //根据基金代码分组(按净值日期倒序排列)
        Map<String,List<FundNetVal>>  fundListMap=new HashMap<>();
        if(fundNetValArrList!=null){
            for(FundNetVal fundNetVal:fundNetValArrList){
                List<FundNetVal> list=fundListMap.get(fundNetVal.getCode());
                if(list==null){
                    List<FundNetVal> tempList=new ArrayList<>();
                    tempList.add(fundNetVal);
                    fundListMap.put(fundNetVal.getCode(),tempList);
                }else{
                    list.add(fundNetVal);
                    fundListMap.put(fundNetVal.getCode(),list);
                }
            }

        }

        return fundListMap;
    }


    /*
     * 计算每日的收益率以及风险率,insert into table:fund_calculate_data_day
     */
    @Test
    public void calculateDataOfData(){

        //查询计算风险率所需参数（取值数量）
        Integer number=getNumberFromSysConfig(TYPE_OF_DAY);
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=jobTimeService.selectJobTimeRecord(TRIGGER_NAME_OF_DAY);
        Date selectDate=new Date();
        if(jobTimeRecord==null || jobTimeRecord.getTriggerTime()==null){
            try {
                selectDate=sdf.parse(START_QUERY_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            selectDate=jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String,List<FundNetVal>>  fundListMap=selectFundNetValueByDate(selectDate);
        if(fundListMap!=null && fundListMap.size()>0){

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundListMap.entrySet().iterator();

            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double navadj1;//当天净值
                Double navadj2;//前一天净值
                Double yieldRatio=0d;//收益率
                Double riskRatio=0d;//风险率
                Double semiVariance=0d;//半方差

                for(int i=0;i<fundList.size()-1;i++){
                    try{
                        int tempNum=i;
                        //取该天数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取该天前一天数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算收益率
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            navadj1=fundNetVal1.getNavadj();
                            navadj2=fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1,navadj2);

                        }

                        //计算风险率
                        riskRatio=calculateRiskRatio(i,fundList,number);

                        //计算半方差
                        semiVariance=calculateSemiVariance(i,fundList,number);

                        FundCalculateData fundCalculateData =new FundCalculateData();
                        fundCalculateData.setCode(code);//基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate());//净值日期
                        fundCalculateData.setYieldRatio(yieldRatio==null?0d:yieldRatio);//收益率
                        fundCalculateData.setRiskRatio(riskRatio==null?0d:riskRatio);//风险率
                        fundCalculateData.setSemiVariance(semiVariance==null?0d:semiVariance);//半方差

                        try{
                            fundCalculateDataMapper.insertFundCalculateDataDay(fundCalculateData);
                        }catch(Exception e){
                            logger.error("插入基金日计算数据失败：fundCalculateData="+ fundCalculateData.toString());
                            e.printStackTrace();
                        }


                    }catch(Exception e){
                        logger.error("计算基金日收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }


                }

            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate =fundNetValMapper.getMaxNavDateByDate(selectDate);

        JobTimeRecord jobTimeRecordTemp=new JobTimeRecord();

        if(jobTimeRecord==null){

            jobTimeRecordTemp.setJobName(FUND_CALCULATE_JOB);
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_DAY);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setCreateTime(new Date());
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.insertJobTimeRecord(jobTimeRecordTemp);
        }else{
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_DAY);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.updateJobTimeRecord(jobTimeRecordTemp);
        }


    }


    /*
     * 计算每周的收益率以及风险率,insert into table:fund_calculate_data_week
     */
    @Test
    public void calculateDataOfWeek(){

        //查询计算风险率所需参数（取值数量）
        Integer number=getNumberFromSysConfig(TYPE_OF_WEEK);

        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=jobTimeService.selectJobTimeRecord(TRIGGER_NAME_OF_WEEK);
        Date selectDate=new Date();
        if(jobTimeRecord==null || jobTimeRecord.getTriggerTime()==null){
            try {
                selectDate=sdf.parse(START_QUERY_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            selectDate=jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String,List<FundNetVal>>  fundListMap=selectFundNetValueByDate(selectDate);
        if(fundListMap!=null && fundListMap.size()>0){

            //过滤数据（取周五数据）
            Map<String,List<FundNetVal>> fundFriListMap=filterData(fundListMap, TYPE_OF_WEEK);

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundFriListMap.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double navadj1=null;//该周周五净值
                Double navadj2=null;//前一周周五净值
                Double yieldRatio=null;//收益率
                Double riskRatio=null;//风险率
                Double semiVariance=0d;//半方差

                for(int i=0;i<fundList.size()-1;i++){
                    try{
                        int tempNum=i;
                        //取该周周五数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取前一周周五数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算收益率
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            navadj1=fundNetVal1.getNavadj();
                            navadj2=fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1,navadj2);

                        }

                        //计算风险率
                        riskRatio=calculateRiskRatio(i,fundList,number);
                        //计算半方差
                        semiVariance=calculateSemiVariance(i,fundList,number);

                        FundCalculateData fundCalculateData =new FundCalculateData();
                        fundCalculateData.setCode(code);//基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate());//净值日期
                        fundCalculateData.setYieldRatio(yieldRatio==null?0d:yieldRatio);//收益率
                        fundCalculateData.setRiskRatio(riskRatio==null?0d:riskRatio);//风险率
                        fundCalculateData.setSemiVariance(semiVariance==null?0d:semiVariance);//半方差

                        try{
                            fundCalculateDataMapper.insertFundCalculateDataWeek(fundCalculateData);
                        }catch(Exception e){
                            logger.error("插入基金周计算数据失败：fundCalculateData="+ fundCalculateData.toString());
                            e.printStackTrace();
                        }

                    }catch(Exception e){
                        logger.error("计算基金周收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }

                }

            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate =fundNetValMapper.getMaxNavDateByDate(selectDate);
        JobTimeRecord jobTimeRecordTemp=new JobTimeRecord();

        if(jobTimeRecord==null){

            jobTimeRecordTemp.setJobName(FUND_CALCULATE_JOB);
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_WEEK);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setCreateTime(new Date());
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.insertJobTimeRecord(jobTimeRecordTemp);
        }else{
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_WEEK);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.updateJobTimeRecord(jobTimeRecordTemp);
        }

    }


    /*
     * 计算每月的收益率以及风险率,insert into table:fund_calculate_data_month
     */
    @Test
    public void calculateDataOfMonth(){

        //查询计算风险率所需参数（取值数量）
        Integer number=getNumberFromSysConfig(TYPE_OF_MONTH);

        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=jobTimeService.selectJobTimeRecord(TRIGGER_NAME_OF_MONTH);
        Date selectDate=new Date();
        if(jobTimeRecord==null || jobTimeRecord.getTriggerTime()==null){
            try {
                selectDate=sdf.parse(START_QUERY_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            selectDate=jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String,List<FundNetVal>>  fundListMap=selectFundNetValueByDate(selectDate);
        if(fundListMap!=null && fundListMap.size()>0){

            //过滤数据（取每月底数据）
            Map<String,List<FundNetVal>> fundFriListMap=filterData(fundListMap, TYPE_OF_MONTH);

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundFriListMap.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double navadj1=null;//该月月底净值
                Double navadj2=null;//前一月月底净值
                Double yieldRatio=null;//收益率
                Double riskRatio=null;//风险率
                Double semiVariance=0d;//半方差

                for(int i=0;i<fundList.size()-1;i++){
                    try{
                        int tempNum=i;
                        //取该月月底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取前一月月底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算收益率
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            navadj1=fundNetVal1.getNavadj();
                            navadj2=fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1,navadj2);

                        }

                        //计算风险率
                        riskRatio=calculateRiskRatio(i,fundList,number);
                        //计算半方差
                        semiVariance=calculateSemiVariance(i,fundList,number);

                        FundCalculateData fundCalculateData =new FundCalculateData();
                        fundCalculateData.setCode(code);//基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate());//净值日期
                        fundCalculateData.setYieldRatio(yieldRatio==null?0d:yieldRatio);//收益率
                        fundCalculateData.setRiskRatio(riskRatio==null?0d:riskRatio);//风险率
                        fundCalculateData.setSemiVariance(semiVariance==null?0d:semiVariance);//半方差

                        try{
                            fundCalculateDataMapper.insertFundCalculateDataMonth(fundCalculateData);
                        }catch(Exception e){
                            logger.error("插入基金月计算数据失败：fundCalculateData="+ fundCalculateData.toString());
                            e.printStackTrace();
                        }


                    }catch(Exception e){
                        logger.error("计算基金月收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }


                }

            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate =fundNetValMapper.getMaxNavDateByDate(selectDate);
        JobTimeRecord jobTimeRecordTemp=new JobTimeRecord();

        if(jobTimeRecord==null){

            jobTimeRecordTemp.setJobName(FUND_CALCULATE_JOB);
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_MONTH);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setCreateTime(new Date());
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.insertJobTimeRecord(jobTimeRecordTemp);
        }else{
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_MONTH);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.updateJobTimeRecord(jobTimeRecordTemp);
        }

    }


    /*
     * 计算每年的收益率以及风险率,insert into table:fund_calculate_data_year
     */
    @Test
    public void calculateDataOfYear(){

        //查询计算风险率所需参数（取值数量）
        Integer number=getNumberFromSysConfig(TYPE_OF_YEAR);
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=jobTimeService.selectJobTimeRecord(TRIGGER_NAME_OF_MONTH);
        Date selectDate=new Date();
        if(jobTimeRecord==null || jobTimeRecord.getTriggerTime()==null){
            try {
                selectDate=sdf.parse(START_QUERY_DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            selectDate=jobTimeRecord.getTriggerTime();
        }
        //查询净值数据
        Map<String,List<FundNetVal>>  fundListMap=selectFundNetValueByDate(selectDate);
        //查询基金净值数据
        if(fundListMap!=null && fundListMap.size()>0){

            //过滤数据（取每年年底数据）
            Map<String,List<FundNetVal>> fundFriListMap=filterData(fundListMap, TYPE_OF_YEAR);

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundFriListMap.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double navadj1=null;//该年年底净值
                Double navadj2=null;//前一年年底净值
                Double yieldRatio=null;//收益率
                Double riskRatio=null;//风险率
                Double semiVariance=0d;//半方差

                for(int i=0;i<fundList.size()-1;i++){
                    try{
                        int tempNum=i;
                        //取该年年底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取前一年年底数据（没有则往之前时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算收益率
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            navadj1=fundNetVal1.getNavadj();
                            navadj2=fundNetVal2.getNavadj();
                            //调用计算收益率方法
                            yieldRatio = calculateYieldRatio(navadj1,navadj2);

                        }

                        //计算风险率
                        riskRatio=calculateRiskRatio(i,fundList,number);
                        //计算半方差
                        semiVariance=calculateSemiVariance(i,fundList,number);

                        FundCalculateData fundCalculateData =new FundCalculateData();
                        fundCalculateData.setCode(code);//基金代码
                        fundCalculateData.setNavDate(fundNetVal1.getNavLatestDate());//净值日期
                        fundCalculateData.setYieldRatio(yieldRatio==null?0d:yieldRatio);//收益率
                        fundCalculateData.setRiskRatio(riskRatio==null?0d:riskRatio);//风险率
                        fundCalculateData.setSemiVariance(semiVariance==null?0d:semiVariance);//半方差

                        try{
                            fundCalculateDataMapper.insertFundCalculateDataYear(fundCalculateData);
                        }catch(Exception e){
                            logger.error("插入基金年计算数据失败：fundCalculateData="+ fundCalculateData.toString());
                            e.printStackTrace();
                        }


                    }catch(Exception e){
                        logger.error("计算基金年收益率以及风险率失败：code="+code);
                        e.printStackTrace();
                    }


                }

            }
        }

        //记录本次TriggerJob查询到的最大净值日期
        Date maxDate =fundNetValMapper.getMaxNavDateByDate(selectDate);
        JobTimeRecord jobTimeRecordTemp=new JobTimeRecord();

        if(jobTimeRecord==null){

            jobTimeRecordTemp.setJobName(FUND_CALCULATE_JOB);
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_YEAR);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setCreateTime(new Date());
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.insertJobTimeRecord(jobTimeRecordTemp);
        }else{
            jobTimeRecordTemp.setTriggerName(TRIGGER_NAME_OF_YEAR);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.updateJobTimeRecord(jobTimeRecordTemp);
        }

    }



    /*
     * 查询配置参数
     */
    public Integer getNumberFromSysConfig(String type){
        Integer number=null;
        try{
            number=fundNetValMapper.getNumberFromSysConfig(type);
        }catch(Exception e){
            logger.error("查询配置数据失败：type="+type);
        }
        if(number==null){
            number=20;//默认
        }

        return number;
    }


    /*
     *取到有效净值数据（没有则往之前时间递推）
     */

    public FundNetVal getEffectData(int i,List<FundNetVal> fundList){
        if(0<=i && i<fundList.size()){
            FundNetVal fundNetVal=fundList.get(i);
            while(fundNetVal==null || fundNetVal.getNavadj()==null ) {
                int temp=i++;
                if(temp<fundList.size()){
                    fundNetVal=fundList.get(temp);
                }else{
                    fundNetVal=null;//fundList 遍历结束仍无有效数据，赋值为null,结束循环
                    break;
                }
            }
            return fundNetVal;

        }else{
            return null;
        }

    }



    /*
     * 过滤数据
     */
    public  Map<String,List<FundNetVal>> filterData(Map<String,List<FundNetVal>> fundListMap,String type){
        Map<String,List<FundNetVal>> fundFriDataMap=new HashMap<>();
        Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundListMap.entrySet().iterator();
        while (entries.hasNext()) {

            Map.Entry<String, List<FundNetVal>> entry = entries.next();
            String code = entry.getKey();
            List<FundNetVal> fundList = entry.getValue();
            //对fundList 按时间进项过滤筛选
            if(fundList!=null){
                for(int i=0;i<fundList.size();i++){
                    int tempNum=i;
                    FundNetVal fundNetVal=fundList.get(i);
                    if(fundNetVal!=null && fundNetVal.getNavLatestDate()!=null && fundNetVal.getNavadj()!=null){
                        List<FundNetVal> list=fundFriDataMap.get(code);
                        Date navLatestDate=fundNetVal.getNavLatestDate();
                        if(list==null){
                            List<FundNetVal> tempList=new ArrayList<>();

                            if(TYPE_OF_WEEK.equals(type)){
                                //取周五数据
                                FundNetVal tempFundNetVal=getFriData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    tempList.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_MONTH.equals(type)){
                                //取每月底数据
                                FundNetVal tempFundNetVal=getMonthData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    tempList.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_YEAR.equals(type)){
                                //取每年年底数据
                                FundNetVal tempFundNetVal=getYearData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    tempList.add(tempFundNetVal);
                                }
                            }


                            fundFriDataMap.put(code,tempList);
                        }else{

                            if(TYPE_OF_WEEK.equals(type)){
                                //取周五数据
                                FundNetVal tempFundNetVal=getFriData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    list.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_MONTH.equals(type)){
                                //取每月底数据
                                FundNetVal tempFundNetVal=getMonthData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    list.add(tempFundNetVal);
                                }
                            }else if(TYPE_OF_YEAR.equals(type)){
                                //取每年年底数据
                                FundNetVal tempFundNetVal=getYearData(fundList,navLatestDate,tempNum);
                                if(tempFundNetVal!=null){
                                    list.add(tempFundNetVal);
                                }
                            }


                            fundFriDataMap.put(code,list);
                        }

                    }

                }
                if(TYPE_OF_MONTH.equals(type)){
                    monthMap=new HashMap<>();
                }else if(TYPE_OF_YEAR.equals(type)){
                    yearMap=new HashMap<>();
                }

            }


        }

        return fundFriDataMap;
    }


    /*
     * 取周五数据，若无则往前递推
     */
    public FundNetVal  getFriData(List<FundNetVal> fundList,Date navLatestDate,int tempNum){
        FundNetVal fundNetVal=fundList.get(tempNum);
        //navLatestDate: Tue Nov 14 00:00:00 CST 2017
        String[] navLatestDateArr =navLatestDate.toString().split(" ");
        if(navLatestDateArr.length==6){
            String weekday=navLatestDateArr[0];
            if(WEEKDAY_OF_FRI.equals(weekday)){
                countOfWeek=0;
                return fundNetVal;
            }else{
                ++countOfWeek;
                if(countOfWeek<7){//一周七天
                    return null;
                }else{
                    countOfWeek=0;
                    FundNetVal tempFundNetVal=getEffectData(tempNum-2,fundList);//一周之内找不到周五数据，则取该周周尾有效数据
                    return tempFundNetVal;
                }

            }
        }else{
            return null;
        }


    }

    /*
     * 取每月月底数据
     */
    public FundNetVal  getMonthData(List<FundNetVal> fundList,Date navLatestDate,int tempNum){
        FundNetVal fundNetVal=fundList.get(tempNum);
        String navLatestDateStr =sdf.format(navLatestDate);
        String[] navLatestDateArr =navLatestDateStr.split("-");
        if(navLatestDateArr.length==3){
            String tag=navLatestDateArr[0]+navLatestDateArr[1];
            if( monthMap.get(tag)==null){
                monthMap.put(tag,fundNetVal);
                return fundNetVal;
            }

        }else{
            return null;
        }
        return null;
    }

    /*
     * 取每年年底数据
     */
    public FundNetVal  getYearData(List<FundNetVal> fundList,Date navLatestDate,int tempNum){
        FundNetVal fundNetVal=fundList.get(tempNum);
        String navLatestDateStr =sdf.format(navLatestDate);
        String[] navLatestDateArr =navLatestDateStr.split("-");
        if(navLatestDateArr.length==3){
            String tag=navLatestDateArr[0];
            if( yearMap.get(tag)==null){
                yearMap.put(tag,fundNetVal);
                return fundNetVal;
            }

        }else{
            return null;
        }
        return null;
    }

    /*
     * 计算收益率方法
     */
    public Double calculateYieldRatio(Double navadj1,Double navadj2){
        Double yieldRatio=0d;
        if(navadj2!=0){
            yieldRatio = Math.log(navadj1/navadj2);

        }
        return yieldRatio;

    }

    /*
     * 计算风险率方法
     */
    public Double calculateRiskRatio(int i,List<FundNetVal> fundList,int number){
        List<Double> tempList=new ArrayList<>();
        //取值
        while(tempList.size()<number){
            FundNetVal fundNetVal=getEffectData(i,fundList);
            if(fundNetVal!=null){
                tempList.add(fundNetVal.getNavadj());
            }
            i++;
            if(i>=fundList.size()){
                break;
            }
        }
        //计算风险率(样本标准差)
        Double riskRatio=StandardDiviation(tempList.toArray(new Double[0]));

        return riskRatio;

    }


    /*
     * 计算半方差方法
     */
    public Double calculateSemiVariance(int i,List<FundNetVal> fundList,int number){
        List<Double> tempList=new ArrayList<>();
        //取值
        while(tempList.size()<number){
            FundNetVal fundNetVal=getEffectData(i,fundList);
            if(fundNetVal!=null){
                tempList.add(fundNetVal.getNavadj());
            }
            i++;
            if(i>=fundList.size()){
                break;
            }
        }

        Double paramVal=1.1;//传入参数
        //计算半方差
        Double semiVariance=semiVariance(tempList.toArray(new Double[0]),paramVal);

        return semiVariance;

    }


    //计算风险率(样本标准差 (n-1) )
    public  Double StandardDiviation(Double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        if(m>1){
            return Math.sqrt(dVar/(m-1));
        }
        return 0d;
    }


    /*
     * 计算半方差
     */
    public Double semiVariance(Double[] x,Double paramVal){

        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;

        for(int i=0;i<m;i++){//求方差
            if(x[i]<paramVal){
                dVar+=(x[i]-dAve)*(x[i]-dAve);
            }
        }
        if(m>1){
            return Math.sqrt(dVar/(m-1));
        }
        return 0d;

    }




}