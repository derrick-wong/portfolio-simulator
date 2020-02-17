package com.portfolio.simulator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SimulationResult {
  private HashMap<Integer, List<BigDecimal>> projectionResults;
  private Portfolio portfolio;
}
