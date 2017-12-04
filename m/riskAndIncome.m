function [PortRisk,PortReturn]=riskAndIncome(ExpReturn,ExpCovariance,PortWts)
[PortRisk,PortReturn]=portstats(ExpReturn,ExpCovariance,PortWts);