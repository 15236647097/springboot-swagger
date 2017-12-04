function[RetSeries,RetIntervals]=calculatePriceToYield(TickSeries,TickTime,Method)
%输入变量：
%TickSeries   价格序列
%TickTime     时间价格序列
%Method       收益率计算方法，分为Simple，Continuous
%输出变量：
%RetSeries     收益率序列
%RetIntervals  收益率时间序列
[RetSeries,RetIntervals]=tick2ret(TickSeries,TickTime,Method);

display(RetSeries);
display(RetIntervals);