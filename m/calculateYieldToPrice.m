function[TickSeries,TickTimes]=calculateYieldToPrice(RetSeries,StartPrice,RetIntervals,StartTime,Method)
%���������
%RetSeries     ����������
%StartPrice    ��ѡ����ʼ�۸�Ĭ��Ϊ1
%RetIntervals  ������ʱ������
%StartTime     ��ʼʱ��
%Method        �����ʼ��㷽������ΪSimple��Continuous
 
%���������
%TickSeries    �۸�����
%TickTime      ʱ��۸�����
[TickSeries,TickTimes]=ret2tick(RetSeries,StartPrice,RetIntervals,StartTime,Method);
display(TickSeries);
display(TickTimes);

