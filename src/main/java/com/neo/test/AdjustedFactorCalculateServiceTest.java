package com.neo.test;

import com.neo.entity.FundNetVal;
import com.neo.mapper.FundNetValMapper;
import com.neo.job.entity.JobTimeRecord;
import com.neo.job.service.JobTimeService;
import com.neo.secvice.FundCalculateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static util.ConstantUtil.*;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/25
 * Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class AdjustedFactorCalculateServiceTest {

    @Autowired
    private JobTimeService jobTimeService;
    @Autowired
    private FundCalculateService fundCalculateService;
    @Autowired
    private FundNetValMapper fundNetValMapper;

    private static final Logger logger= LoggerFactory.getLogger(AdjustedFactorCalculateServiceTest.class);

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void calculateAdjustedFactor(){
        //查询TriggerJob 上次执行时间
        JobTimeRecord jobTimeRecord=jobTimeService.selectJobTimeRecord(ADJUSTED_FACTOR_TRIGGER);
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

        //根据日期扫描查询数据
        Map<String,List<FundNetVal>> fundListMap=fundCalculateService.selectFundNetValueByDate(selectDate);
        if(fundListMap!=null && fundListMap.size()>0){

            Iterator<Map.Entry<String,List<FundNetVal>>> entries = fundListMap.entrySet().iterator();

            while (entries.hasNext()) {

                Map.Entry<String,List<FundNetVal>> entry = entries.next();

                String code=entry.getKey();
                List<FundNetVal> fundList=entry.getValue();
                Double unitnav1;//当天单位净值
                Double unitnav2;//前一天单位净值
                Double navaccum1;//当天累计单位净值
                Double navaccum2;//前一天累计单位净值
                Double adjustedFactor1=1d;//当天复权因子
                Double adjustedFactor2;//前一天复权因子

                //减 2 是因为fundList.size()-1 对应数据上一次job 已经处理过，本次该数据作为计算参考用
                for(int i=fundList.size()-2;i>=0;i--){
                    try{
                        int tempNum=i;
                        //取该天数据（没有则往之后时间递推）
                        FundNetVal fundNetVal1=getEffectData(tempNum,fundList);
                        //取该天前一天数据（没有则往之后时间递推）
                        FundNetVal fundNetVal2=getEffectData(++tempNum,fundList);

                        //计算复权因子
                        if(fundNetVal1!=null && fundNetVal2!=null){
                            unitnav1=fundNetVal1.getUnitNav();
                            unitnav2=fundNetVal2.getUnitNav();

                            navaccum1=fundNetVal1.getNavAccum();
                            navaccum2=fundNetVal2.getNavAccum();

                            adjustedFactor2=fundNetVal2.getAdjustedFactor();

                            //调用计算复权因子方法
                            adjustedFactor1 = calculateAdjustedFactor(unitnav1,unitnav2,navaccum1,navaccum2,adjustedFactor2);

                            fundNetVal1.setAdjustedFactor(adjustedFactor1);

                            try{
                                fundNetValMapper.updateAdjustedFactor(fundNetVal1);
                            }catch(Exception e){
                                logger.error("插入基金复权因子数据失败：fundNetVal="+ fundNetVal1.toString());
                                e.printStackTrace();
                            }

                        }

                    }catch(Exception e){
                        logger.error("计算基金复权因子失败：code="+code);
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
            jobTimeRecordTemp.setTriggerName(ADJUSTED_FACTOR_TRIGGER);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setCreateTime(new Date());
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.insertJobTimeRecord(jobTimeRecordTemp);
        }else{
            jobTimeRecordTemp.setTriggerName(ADJUSTED_FACTOR_TRIGGER);
            jobTimeRecordTemp.setTriggerTime(maxDate);
            jobTimeRecordTemp.setUpdateTime(new Date());

            jobTimeService.updateJobTimeRecord(jobTimeRecordTemp);
        }

    }


    /*
     *取到有效净值数据（没有则往之后时间递推）
     */
    public FundNetVal getEffectData(int i,List<FundNetVal> fundList){
        if(0<=i && i<fundList.size()){
            FundNetVal fundNetVal=fundList.get(i);
            while(fundNetVal==null || fundNetVal.getUnitNav()==null
                  ||fundNetVal.getNavAccum()==null /*||fundNetVal.getAdjustedFactor()==null*/) {
                int temp=i--;
                if(0<=temp && temp<fundList.size()){
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
     * 计算复权因子方法
     */
    public Double calculateAdjustedFactor(Double unitnav1,Double unitnav2,Double navaccum1,Double navaccum2,Double adjustedFactor2){
        Double adjustedFactor=1d;//复权因子

        if(unitnav1!=0 && adjustedFactor2!=null && adjustedFactor2!=0){
            adjustedFactor=(navaccum1-(navaccum2-unitnav2))/(unitnav1*adjustedFactor2);
        }

        return adjustedFactor;
    }

    

}