package com.neo.mapper;

import com.neo.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public interface FundGroupMapper {

    List<Interval> selectFundGroup(Map map);

    int insertRecommendHistory(Map map);

    List<Interval> selectAllFundGroup();

    List<Interval> selectAllFundGroupNum();

    List<Interval> selectById(@Param("id") String id,@Param("subGroupId") String subGroupId);

    List<Interval> getProportion(String id);

    List<FundGroupDetails> efficientFrontier(String id);

    List<Interval> getinterval(Map map);

    int updateStatus(Map map);

    int insertFundGroup(FundGroup fundGroup);

    int insertFundGroupDetail(List<FundGroupDetails> fundGroupDetailslist);

    Interval selectReturnAndPullback(Map map);

    List<RiskIncomeInterval> getPerformanceVolatility(Map map);

    List<Interval> getRevenueContribution(Map map);

    List<RiskController> getRiskController(@Param("id") String id,@Param("subGroupId") String subGroupId);

    List<RiskIncomeInterval> getScaleMark(@Param("id") String id);

    List<FundGroupBuy> getFundGroupBuy(@Param("id") String id);

    List<FundNetValue> getFundNetValue(@Param("id") String id,@Param("starttime") String starttime,@Param("endtime") String endtime);

}
