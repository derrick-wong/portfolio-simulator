package com.portfolio.simulator.service.impl;

import com.portfolio.simulator.domain.Portfolio;
import com.portfolio.simulator.domain.SimulationParams;
import com.portfolio.simulator.domain.SimulationResult;
import com.portfolio.simulator.domain.TargetAllocation;
import com.portfolio.simulator.exception.InvalidSimulationParamsException;
import com.portfolio.simulator.service.Simulator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class MonteCarloSimulator implements Simulator {

  final Random random = new Random();

  private static final Integer INITIAL_YEAR = 0;
  private static final String INVALID_PARAMS_MSG = "Cannot run simulation with invalid parameters";

  @Override
  public SimulationResult run(Portfolio portfolio, SimulationParams params) throws InvalidSimulationParamsException {

    if (!isValidPortfolio(portfolio) || !isValidSimulationParams(params)) {
      throw new InvalidSimulationParamsException(INVALID_PARAMS_MSG);
    }
    TargetAllocation targetAllocation = portfolio.getTargetAllocation();
    HashMap<Integer, List<BigDecimal>> monteCarloSimulationResults = new HashMap<>();

    for (int sim = 0; sim < params.getNumOfSimulations(); sim++) {
      BigDecimal randomInterestRate = BigDecimal.valueOf(
        random.nextGaussian() * targetAllocation.getHistoricalRisk().doubleValue()
          + targetAllocation.getHistoricalReturn().doubleValue());

      List<BigDecimal> simulatedReturns = simulateYearlyReturns(
        randomInterestRate, portfolio.getBalance(), params.getInflationRate(), params.getYearsToForecast());
      monteCarloSimulationResults.put(sim, simulatedReturns);
    }

    return SimulationResult.builder()
      .projectionResults(monteCarloSimulationResults)
      .portfolio(portfolio)
      .build();
  }

  private List<BigDecimal> simulateYearlyReturns(BigDecimal interestRate,
                                                 BigDecimal principal,
                                                 BigDecimal inflationRate,
                                                 Integer yearsToForecast) {
    List<BigDecimal> simulatedReturns = new ArrayList<>();
    simulatedReturns.add(INITIAL_YEAR, principal);

    for (int year = 1; year < yearsToForecast; year++) {
      BigDecimal previousYearBalance = simulatedReturns.get(year - 1);
      BigDecimal normInterestRate = interestRate.add(BigDecimal.ONE);
      BigDecimal normInflationRate = BigDecimal.ONE.subtract(inflationRate);
      BigDecimal adjustedReturn = previousYearBalance.multiply(normInterestRate).multiply(normInflationRate);
      simulatedReturns.add(year, adjustedReturn);
    }
    return simulatedReturns;
  }

  private boolean isValidPortfolio(Portfolio portfolio) {
    return !(portfolio == null
      || portfolio.getBalance().compareTo(BigDecimal.ZERO) <= 0
      || portfolio.getTargetAllocation() == null);
  }

  private boolean isValidSimulationParams(SimulationParams params) {
    return !(params == null || params.getYearsToForecast() <= 0 || params.getNumOfSimulations() <= 0);
  }
}
