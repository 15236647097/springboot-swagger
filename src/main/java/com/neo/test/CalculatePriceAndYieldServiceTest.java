package com.neo.test;

import com.mathworks.toolbox.javabuilder.MWException;
import com.yihui.MatLab;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/28
 * Desc:价格收益率互相转换计算
 */
public class CalculatePriceAndYieldServiceTest {

    private static final Logger logger= LoggerFactory.getLogger(CalculatePriceAndYieldServiceTest.class);

    /*
     * 价格转收益率
     */
    @Test
    public void test(){


        Object[] result=null;

        List<Double> retSeriesList=new ArrayList<>();

        List<Double> tickSeriesList=new ArrayList<>();

        List<Double> tickSeries =new ArrayList<>();
        tickSeries.add(100d);
        tickSeries.add(110d);
        tickSeries.add(120d);
        tickSeries.add(120d);

        double[][] tickTime  ={};//时间价格序列
        String method="Simple";//计算利息方式 必须为'Simple' (默认值) 或'Continuous'
        retSeriesList=calculatePriceToYield(tickSeries,method);



        double startPrice=1;//初始价格，默认为1
        double[][] retIntervals  ={};//收益率时间序列
        String startTime="2011-11-11";//开始时间
        String method1="Simple";//计算利息方式 必须为'Simple' (默认值) 或'Continuous'

        List<Double> retSeries =new ArrayList<>();
        retSeries.add(0.1);
        retSeries.add(0.05);
        retSeries.add(-0.05);
        tickSeriesList=calculateYieldToPrice(retSeries,startPrice,method1);



    }


    public List<Double> calculatePriceToYield(List<Double> tickSeries,String method){

        double[][] tickTime  ={};//时间价格序列
        Object[] result=null;
        String[] retSeries=null; //收益率序列
        List<Double> retSeriesList=new ArrayList<>();

        Double[][] tickSeriesArr=new Double[tickSeries.size()][1];
        for(int i=0;i<tickSeries.size();i++){
            tickSeriesArr[i][0]=tickSeries.get(i);
        }

        try {
            MatLab matLab= new MatLab();
            result=matLab.calculatePriceToYield(1,tickSeriesArr,tickTime,method);

            if(result!=null && result[0]!=null){
                retSeries=result[0].toString().split("\n");

                for(String a:retSeries){
                    retSeriesList.add(Double.parseDouble(a));
                }

            }

        } catch (MWException e) {
            logger.debug("Failed to calculatePriceToYield");
            e.printStackTrace();
        }

        return retSeriesList;

    }


    /*
     * 收益率转价格
     * param:retSeries 收益率序列
     *       startPrice 初始价格，默认为1
     *       retIntervals 收益率时间序列
     *       startTime 开始时间
     *       method 计算利息方式 必须为'Simple' (默认值) 或'Continuous'
     */
    public List<Double> calculateYieldToPrice(List<Double> retSeries,double startPrice,String method){

        Object[] result=null;
        String[] tickSeries=null; //价格序列
        List<Double> tickSeriesList=new ArrayList<>();
        double[][] retIntervals  ={};//收益率时间序列
        String startTime="";//开始时间

        Double[][] retSeriesArr=new Double[retSeries.size()][1];
        for(int i=0;i<retSeries.size();i++){
            retSeriesArr[i][0]=retSeries.get(i);
        }

        try {
            MatLab matLab= new MatLab();
            result=matLab.calculateYieldToPrice(1,retSeriesArr,startPrice,retIntervals,startTime,method);

            if(result!=null && result[0]!=null){
                tickSeries=result[0].toString().split("\n");

                for(String a:tickSeries){
                    tickSeriesList.add(Double.parseDouble(a));
                }
            }

        } catch (MWException e) {
            logger.debug("Failed to calculateYieldToPrice");
            e.printStackTrace();
        }

        return tickSeriesList;

    }




}
