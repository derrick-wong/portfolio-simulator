package com.portfolio.simulator.service.impl;

import com.portfolio.simulator.domain.SimulationResult;
import com.portfolio.simulator.exception.InvalidStatsGenerationParamsException;
import com.portfolio.simulator.service.StatsGenerator;
import com.portfolio.simulator.utils.ResultsUtil;
import com.portfolio.simulator.utils.StatsPerformanceUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class MonteCarloStatisticsGenerator implements StatsGenerator<BigDecimal, SimulationResult> {

  private static final String INVALID_PARAMS_MSG = "Invalid simulation results or year - cannot generate statistics";

  @Override
  public BigDecimal calcPercentile(double percent, int year, SimulationResult simulationResult)
    throws InvalidStatsGenerationParamsException {
    if (simulationResult == null
      || isMapEmptyOrNull(simulationResult.getProjectionResults())
      || percent < 0 || percent > 1
      || year < 0) {
      throw new InvalidStatsGenerationParamsException(INVALID_PARAMS_MSG);
    }
    BigDecimal[] filteredResults = ResultsUtil.retrieveResultsByYear(simulationResult.getProjectionResults(), year);
    return StatsPerformanceUtil.derivePercentile(filteredResults, percent);
  }

  private boolean isMapEmptyOrNull(HashMap<?, ?> map) {
    return map == null || map.isEmpty();
  }
}
