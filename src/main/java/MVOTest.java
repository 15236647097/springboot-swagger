import util.MVO;

/**
 * Created by wangyinuo on 2017/11/22.
 */
public class MVOTest {
    public Object[] efficientFrontier(){

        Object[] resust = null;
        Double [] ExpReturn = { 0.0054, 0.0531, 0.0779, 0.0934, 0.0130 };
        Double[][] ExpCovariance = {{0.0569,  0.0092,  0.0039,  0.0070,  0.0022},
                {0.0092,  0.0380,  0.0035,  0.0197,  0.0028},
                {0.0039,  0.0035,  0.0997,  0.0100,  0.0070},
                {0.0070,  0.0197,  0.0100,  0.0461,  0.0050},
                {0.0022,  0.0028,  0.0070,  0.0050,  0.0573}};
        //resust = MVO.efficientFrontier(ExpReturn,ExpCovariance,20);
        return resust;
    }

    public Object[] incomeAndRisk(){
        Object[] resust = null;
        Double [] ExpReturn = { 0.0054, 0.0531, 0.0779, 0.0934, 0.0130 };
        Double[][] ExpCovariance = {{0.0569,  0.0092,  0.0039,  0.0070,  0.0022},
                {0.0092,  0.0380,  0.0035,  0.0197,  0.0028},
                {0.0039,  0.0035,  0.0997,  0.0100,  0.0070},
                {0.0070,  0.0197,  0.0100,  0.0461,  0.0050},
                {0.0022,  0.0028,  0.0070,  0.0050,  0.0573}};
        Double [] PortWts = { 0.25, 0.25, 0.25, 0.25, 0.25 };
        resust = MVO.incomeAndRisk(ExpReturn,ExpCovariance,PortWts);
        return resust;
    }
}
