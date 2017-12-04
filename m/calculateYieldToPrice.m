function[TickSeries,TickTimes]=calculateYieldToPrice(RetSeries,StartPrice,RetIntervals,StartTime,Method)
%输入变量：
%RetSeries     收益率序列
%StartPrice    可选，初始价格，默认为1
%RetIntervals  收益率时间序列
%StartTime     开始时间
%Method        收益率计算方法，分为Simple，Continuous
 
%输出变量：
%TickSeries    价格序列
%TickTime      时间价格序列
[TickSeries,TickTimes]=ret2tick(RetSeries,StartPrice,RetIntervals,StartTime,Method);
display(TickSeries);
display(TickTimes);

