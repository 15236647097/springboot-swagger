function[RetSeries,RetIntervals]=calculatePriceToYield(TickSeries,TickTime,Method)
%���������
%TickSeries   �۸�����
%TickTime     ʱ��۸�����
%Method       �����ʼ��㷽������ΪSimple��Continuous
%���������
%RetSeries     ����������
%RetIntervals  ������ʱ������
[RetSeries,RetIntervals]=tick2ret(TickSeries,TickTime,Method);

display(RetSeries);
display(RetIntervals);