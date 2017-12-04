function[ValueAtRisk]=calculatePortvrisk(PortReturn,PortRisk,RiskThreshold,PortValue)

ValueAtRisk=portvrisk(PortReturn,PortRisk,RiskThreshold,PortValue);
%输入变量：
%PortReturn   每个投资组合在该期间的预期收益
%PortRisk     每个项目组合的标准偏差,
%RiskThreshold        损失概率
%PortValue   资产组合的总价值
%输出变量：
%ValueAtRisk      投资组合向量中估计的最大损失
display(ValueAtRisk);
