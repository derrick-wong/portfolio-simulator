package com.portfolio.simulator.service.impl;

import com.portfolio.simulator.domain.Portfolio;
import com.portfolio.simulator.domain.PortfolioType;
import com.portfolio.simulator.domain.SimulationParams;
import com.portfolio.simulator.domain.SimulationResult;
import com.portfolio.simulator.exception.InvalidSimulationParamsException;
import com.portfolio.simulator.service.Simulator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MonteCarloSimulatorTest {

  @Autowired
  private Simulator monteCarloSimulator = new MonteCarloSimulator();

  @Test
  public void shouldThrowInvalidSimulationParametersExceptionWhenSimParamsAreNull() {
    Portfolio testPortfolio = new Portfolio(PortfolioType.VERY_CONSERVATIVE, BigDecimal.ONE);
    Exception exception = assertThrows(InvalidSimulationParamsException.class, () ->
      monteCarloSimulator.run(testPortfolio, null));

    assertThat(exception.getClass(), is(InvalidSimulationParamsException.class));
    assertThat(exception.getMessage(), is("Cannot run simulation with invalid parameters"));
  }

  @Test
  public void shouldThrowInvalidSimulationParametersExceptionWhenPortfolioIsNull() {
    SimulationParams simulationParams = SimulationParams.builder()
      .numOfSimulations(1)
      .inflationRate(BigDecimal.ONE)
      .yearsToForecast(1)
      .build();

    Exception exception = assertThrows(InvalidSimulationParamsException.class, () ->
      monteCarloSimulator.run(null, simulationParams));

    assertThat(exception.getClass(), is(InvalidSimulationParamsException.class));
    assertThat(exception.getMessage(), is("Cannot run simulation with invalid parameters"));
  }

  @Test
  public void shouldThrowInvalidSimulationParametersExceptionWhenPortfolioBalanceIsLessThanEqualToZero() {
    Portfolio invalidZeroBalancePortfolio = new Portfolio(PortfolioType.AGGRESSIVE, BigDecimal.ZERO);
    Portfolio invalidNegativeBalancePortfolio = new Portfolio(PortfolioType.AGGRESSIVE, BigDecimal.valueOf(-1.00));

    SimulationParams invalidZeroBalanceParams = SimulationParams.builder()
      .numOfSimulations(1)
      .inflationRate(BigDecimal.ONE)
      .yearsToForecast(1)
      .build();

    SimulationParams invalidNegativeBalanceParams = SimulationParams.builder()
      .numOfSimulations(1)
      .inflationRate(BigDecimal.ONE)
      .yearsToForecast(1)
      .build();

    Exception zeroBalanceInvalidException = assertThrows(InvalidSimulationParamsException.class, () ->
      monteCarloSimulator.run(invalidZeroBalancePortfolio, invalidZeroBalanceParams));
    Exception negativeBalanceInvalidException = assertThrows(InvalidSimulationParamsException.class, () ->
      monteCarloSimulator.run(invalidNegativeBalancePortfolio, invalidNegativeBalanceParams));

    assertThat(zeroBalanceInvalidException.getClass(), is(InvalidSimulationParamsException.class));
    assertThat(negativeBalanceInvalidException.getMessage(), is("Cannot run simulation with invalid parameters"));
  }

  @Test
  public void shouldThrowInvalidSimulationParametersExceptionWhenRequestedForecastIsZero() {
    Portfolio testPortfolio = new Portfolio(PortfolioType.AGGRESSIVE, BigDecimal.ONE);
    SimulationParams simulationParams = SimulationParams.builder()
      .numOfSimulations(1)
      .inflationRate(BigDecimal.ONE)
      .build();

    Exception exception = assertThrows(InvalidSimulationParamsException.class, () ->
      monteCarloSimulator.run(testPortfolio, simulationParams));

    assertThat(exception.getClass(), is(InvalidSimulationParamsException.class));
    assertThat(exception.getMessage(), is("Cannot run simulation with invalid parameters"));
  }

  @Test
  public void shouldThrowInvalidSimulationParametersExceptionWhenNumOfSimulationsIsZero() {
    Portfolio testPortfolio = new Portfolio(PortfolioType.AGGRESSIVE, BigDecimal.ONE);
    SimulationParams simulationParams = SimulationParams.builder()
      .inflationRate(BigDecimal.ONE)
      .yearsToForecast(1)
      .build();

    Exception exception = assertThrows(InvalidSimulationParamsException.class, () ->
      monteCarloSimulator.run(testPortfolio, simulationParams));

    assertThat(exception.getClass(), is(InvalidSimulationParamsException.class));
    assertThat(exception.getMessage(), is("Cannot run simulation with invalid parameters"));
  }

  @Test
  public void shouldRunSimulationSuccessfullyUsingValidSimulationParams() throws InvalidSimulationParamsException {
    int numberOfSimulations = 5;
    int yearsToForecast = 3;
    BigDecimal interestRate = BigDecimal.valueOf(2.00);
    BigDecimal initialBalance = BigDecimal.valueOf(100.00);
    Portfolio testPortfolio = new Portfolio(PortfolioType.VERY_CONSERVATIVE, initialBalance);

    SimulationParams simulationParams = SimulationParams.builder()
      .yearsToForecast(yearsToForecast)
      .inflationRate(interestRate)
      .numOfSimulations(numberOfSimulations)
      .build();

    SimulationResult simResults = monteCarloSimulator.run(testPortfolio, simulationParams);

    assertNotNull(simResults);
    assertThat(simResults.getPortfolio(), is(testPortfolio));
    assertThat(simResults.getPortfolio().getType(), is(PortfolioType.VERY_CONSERVATIVE));
    assertThat(simResults.getProjectionResults().size(), is(numberOfSimulations));
    simResults.getProjectionResults().forEach((k, v) -> {
      assertThat(v.size(), is(yearsToForecast));
      assertThat(v.get(0), is(initialBalance));
    });
  }

  @Test
  public void shouldEnsureSimulationDoesNotModifyInitialYearValue() throws InvalidSimulationParamsException {
    int numberOfSimulations = 5;
    int yearsToForecast = 1;
    BigDecimal interestRate = BigDecimal.valueOf(2.00);
    BigDecimal initialBalance = BigDecimal.valueOf(100.00);
    Portfolio testPortfolio = new Portfolio(PortfolioType.AGGRESSIVE, initialBalance);

    SimulationParams simulationParams = SimulationParams.builder()
      .yearsToForecast(yearsToForecast)
      .inflationRate(interestRate)
      .numOfSimulations(numberOfSimulations)
      .build();

    SimulationResult simResults = monteCarloSimulator.run(testPortfolio, simulationParams);

    assertNotNull(simResults);
    assertThat(simResults.getPortfolio(), is(testPortfolio));
    assertThat(simResults.getPortfolio().getType(), is(PortfolioType.AGGRESSIVE));
    assertThat(simResults.getProjectionResults().size(), is(numberOfSimulations));
    simResults.getProjectionResults().forEach((k, v) -> {
      assertThat(v.size(), is(yearsToForecast));
      assertThat(v.get(0), is(initialBalance));
    });
  }

  @SpringBootApplication
  static class TestConfiguration {
  }
}
