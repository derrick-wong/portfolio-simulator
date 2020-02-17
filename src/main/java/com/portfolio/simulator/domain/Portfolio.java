package com.portfolio.simulator.domain;

import lombok.Getter;

import java.math.BigDecimal;

import static com.portfolio.simulator.domain.PortfolioType.AGGRESSIVE;
import static com.portfolio.simulator.domain.PortfolioType.VERY_CONSERVATIVE;

@Getter
public class Portfolio {
  private PortfolioType type;
  private BigDecimal balance;
  private TargetAllocation targetAllocation;

  public Portfolio(PortfolioType type, BigDecimal balance) {
    BigDecimal historicalReturn = BigDecimal.ZERO;
    BigDecimal historicalRisk = BigDecimal.ONE;

    if (type.equals(AGGRESSIVE)) {
      historicalReturn = BigDecimal.valueOf(0.094324);
      historicalRisk = BigDecimal.valueOf(0.15675);
    } else if (type.equals(VERY_CONSERVATIVE)) {
      historicalReturn = BigDecimal.valueOf(0.06189);
      historicalRisk = BigDecimal.valueOf(0.063438);
    }

    this.type = type;
    this.balance = balance;
    this.targetAllocation = TargetAllocation.builder()
      .historicalRisk(historicalRisk)
      .historicalReturn(historicalReturn)
      .build();
  }
}
