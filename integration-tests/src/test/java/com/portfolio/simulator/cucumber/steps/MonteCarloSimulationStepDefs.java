package com.portfolio.simulator.cucumber.steps;

import com.portfolio.simulator.domain.Portfolio;
import com.portfolio.simulator.domain.PortfolioType;
import com.portfolio.simulator.domain.SimulationParams;
import com.portfolio.simulator.domain.SimulationResult;
import com.portfolio.simulator.service.Simulator;
import com.portfolio.simulator.service.StatsGenerator;
import com.portfolio.simulator.service.impl.MonteCarloSimulator;
import com.portfolio.simulator.service.impl.MonteCarloStatisticsGenerator;
import io.cucumber.java8.En;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MonteCarloSimulationStepDefs implements En {

  private StatsGenerator<BigDecimal, SimulationResult> monteCarloStatsGenerator = new MonteCarloStatisticsGenerator();

  private Portfolio portfolio;
  private BigDecimal inflationRate;
  private SimulationParams simulationParams;
  private SimulationResult simulationResult;

  public MonteCarloSimulationStepDefs() {
    Given("A user has created an {string} portfolio with principal of {bigdecimal} dollars",
      (String portfolioType, BigDecimal initialInvestment) -> {
        portfolio = new Portfolio(PortfolioType.valueOf(portfolioType), initialInvestment);
      });

    And("The market has an inflationRate of {double}% each year",
      (Double inflationRatePercentage) -> {
        inflationRate = BigDecimal.valueOf(inflationRatePercentage / 100.00);
      });

    When("The user executes a Monte Carlo Simulation with {int} simulations to project future value over {int} years",
      (Integer numOfSimulations, Integer yearsToForecast) -> {
        simulationParams = SimulationParams.builder()
          .inflationRate(inflationRate)
          .yearsToForecast(yearsToForecast)
          .numOfSimulations(numOfSimulations)
          .build();

        Simulator monteCarloSimulator = new MonteCarloSimulator();
        simulationResult = monteCarloSimulator.run(portfolio, simulationParams);
      });

    Then("The user should be able to see the Median value for year {int}",
      (Integer year) -> {
        double fiftiethPercentile = 0.5;
        BigDecimal medianValue = monteCarloStatsGenerator.calcPercentile(fiftiethPercentile, year, simulationResult);

        assertNotNull(medianValue);
        System.out.println("Median value: " + medianValue.setScale(2, RoundingMode.HALF_EVEN));
      });

    And("The user should be able to see the {double}th percentile for year {int}",
      (Double percentage, Integer year) -> {
        double percentile = percentage / 100.00;
        BigDecimal caseValue = monteCarloStatsGenerator.calcPercentile(percentile, year, simulationResult);

        assertNotNull(caseValue);
        System.out.println("Percentile: " + percentile + " - Value: " + caseValue.setScale(2, RoundingMode.HALF_EVEN));
      });

  }
}
