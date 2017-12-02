package com.neo.test;

import com.mathworks.toolbox.javabuilder.MWException;
import com.yihui.MatLab;
import org.junit.Test;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/29
 * Desc:计算组合最大可能损失
 */
public class CalculatePortvriskTest {


    @Test
    public void test(){

     /*
     * 计算组合最大可能损失
     * params:
     *        PortReturn   每个投资组合在该期间的预期收益
     *        PortRisk     每个项目组合的标准偏差
     *        RiskThreshold    损失概率
     *        PortValue   资产组合的总价值
     *
     */
        Double portvrisk=null;
        Double portReturn=7.3/100;
        Double portRisk=5.27 / 100;
//        Double riskThreshold=0.975;
        Double portValue=10000d;
        Double confidenceInterval=0.975;

        portvrisk=calculatePortvrisk(portReturn,portRisk,confidenceInterval,portValue);

//        calculatePortvrisk([0.29/100],[3.08 / 100],0.10,[10000]);



    }



    /*
     * 计算组合最大可能损失
     * params:
     *        portReturn   每个投资组合在该期间的预期收益
     *        portRisk     每个项目组合的标准偏差
     *        confidenceInterval  置信区间
     *        riskThreshold    损失概率= 1 - 置信区间
     *        portValue   资产组合的总价值
     */
    public Double calculatePortvrisk(double portReturn, double portRisk, double confidenceInterval, double portValue){
        Double portvrisk=null;
        Object[] result=null;
        Double riskThreshold=1-confidenceInterval;

        try {
            MatLab matLab= new MatLab();
            result=matLab.calculatePortvrisk(1,portReturn,portRisk,riskThreshold,portValue);

            if(result!=null && result[0]!=null){
                portvrisk=Double.parseDouble(result[0].toString());
            }

        } catch (MWException e) {
//            logger.error("Failed to calculatePortvrisk!");
            e.printStackTrace();
        }

        return portvrisk;
    }

}
