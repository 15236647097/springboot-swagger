package util;

import com.mathworks.toolbox.javabuilder.MWException;
import com.yihui.calculatepricetoyield.CalculatePriceToYield;
import com.yihui.calculateyieldtoprice.CalculateYieldToPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/28
 * Desc:价格收益率互相转换计算（使用 MATLAB）
 */
public class CalculatePriceAndYieldService {

    private static final Logger logger= LoggerFactory.getLogger(CalculatePriceAndYieldService.class);

    /*
     * 价格转收益率
     * param:tickSeries 价格序列
     *       tickTime 时间价格序列
     *       method 计算利息方式 必须为'Simple' (默认值) 或'Continuous'
     *
     */
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
            CalculatePriceToYield calculatePriceToYield=new CalculatePriceToYield();
            result=calculatePriceToYield.calculatePriceToYield(1,tickSeriesArr,tickTime,method);

            if(result!=null && result[0]!=null){
                retSeries=result[0].toString().split("\n");

                for(String a:retSeries){
                    retSeriesList.add(Double.parseDouble(a));
                }

            }

        } catch (MWException e) {
            logger.error("Failed to calculatePriceToYield");
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
            CalculateYieldToPrice calculateYieldToPrice=new CalculateYieldToPrice();
            result=calculateYieldToPrice.calculateYieldToPrice(1,retSeriesArr,startPrice,retIntervals,startTime,method);

            if(result!=null && result[0]!=null){
                tickSeries=result[0].toString().split("\n");

                for(String a:tickSeries){
                    tickSeriesList.add(Double.parseDouble(a));
                }
            }

        } catch (MWException e) {
            logger.error("Failed to calculateYieldToPrice");
            e.printStackTrace();
        }

        return tickSeriesList;

    }



}
