package com.portfolio.simulator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class SimulationParams {
  private int numOfSimulations;
  private int yearsToForecast;
  private BigDecimal inflationRate;
}
