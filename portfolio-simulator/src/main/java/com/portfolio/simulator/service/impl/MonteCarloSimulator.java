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

    HashMap<Integer, List<BigDecimal>> monteCarloSimulationResults = new HashMap<>();
    for (int sim = 0; sim < params.getNumOfSimulations(); sim++) {
      List<BigDecimal> simulatedReturns = simulateYearlyReturns(
        portfolio.getTargetAllocation(),
        portfolio.getBalance(),
        params.getInflationRate(),
        params.getYearsToForecast());
      monteCarloSimulationResults.put(sim, simulatedReturns);
    }

    return SimulationResult.builder()
      .projectionResults(monteCarloSimulationResults)
      .portfolio(portfolio)
      .build();
  }

  private List<BigDecimal> simulateYearlyReturns(TargetAllocation targetAllocation,
                                                 BigDecimal principal,
                                                 BigDecimal inflationRate,
                                                 Integer yearsToForecast) {
    List<BigDecimal> simulatedReturns = new ArrayList<>();
    simulatedReturns.add(INITIAL_YEAR, principal);

    for (int year = 1; year < yearsToForecast; year++) {
      BigDecimal randomInterestRate = BigDecimal.valueOf(
        random.nextGaussian())
        .multiply(targetAllocation.getHistoricalRisk())
        .add(targetAllocation.getHistoricalReturn());

      BigDecimal balance = simulatedReturns.get(year - 1);
      balance = balance.multiply(BigDecimal.ONE.add(randomInterestRate));
      balance = balance.multiply(BigDecimal.ONE.subtract(inflationRate));
      simulatedReturns.add(year, balance);
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
