function[ValueAtRisk]=calculatePortvrisk(PortReturn,PortRisk,RiskThreshold,PortValue)

ValueAtRisk=portvrisk(PortReturn,PortRisk,RiskThreshold,PortValue);
%���������
%PortReturn   ÿ��Ͷ������ڸ��ڼ��Ԥ������
%PortRisk     ÿ����Ŀ��ϵı�׼ƫ��,
%RiskThreshold        ��ʧ����
%PortValue   �ʲ���ϵ��ܼ�ֵ
%���������
%ValueAtRisk      Ͷ����������й��Ƶ������ʧ
display(ValueAtRisk);
