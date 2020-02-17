package com.portfolio.simulator.service.impl;

import com.portfolio.simulator.domain.SimulationResult;
import com.portfolio.simulator.exception.InvalidStatsGenerationParamsException;
import com.portfolio.simulator.service.StatsGenerator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MonteCarloStatisticsGeneratorTest {

  @Autowired
  private StatsGenerator<BigDecimal, SimulationResult> monteCarloStatisticsGenerator;

  @Test
  public void shouldThrowInvalidStatsGenerationParamsExceptionWhenSimResultsAreNull() {

    Exception exception = assertThrows(InvalidStatsGenerationParamsException.class, () ->
      monteCarloStatisticsGenerator.calcPercentile(1,1, null));

    assertThat(exception.getClass(), is(InvalidStatsGenerationParamsException.class));
    assertThat(exception.getMessage(), is("Invalid simulation results or year - cannot generate statistics"));
  }

  @Test
  public void shouldThrowInvalidStatsGenerationParamsExceptionWhenProjectResultsAreNullOrEmpty() {
    SimulationResult nullProjectionResults = SimulationResult.builder().build();
    SimulationResult emptyProjectionResults = SimulationResult.builder()
      .projectionResults(new HashMap<>())
      .build();

    Exception nullProjectionsException = assertThrows(InvalidStatsGenerationParamsException.class, () ->
      monteCarloStatisticsGenerator.calcPercentile(1,1, nullProjectionResults));
    Exception emptyProjectionsException = assertThrows(InvalidStatsGenerationParamsException.class, () ->
      monteCarloStatisticsGenerator.calcPercentile(1,1, emptyProjectionResults));

    assertThat(nullProjectionsException.getClass(), is(InvalidStatsGenerationParamsException.class));
    assertThat(nullProjectionsException.getMessage(), is("Invalid simulation results or year - cannot generate statistics"));

    assertThat(emptyProjectionsException.getClass(), is(InvalidStatsGenerationParamsException.class));
    assertThat(emptyProjectionsException.getMessage(), is("Invalid simulation results or year - cannot generate statistics"));
  }

  @Test
  public void shouldCalculateMedianSuccessfullyGivenOddBigDecimalArray() throws InvalidStatsGenerationParamsException {
    BigDecimal expectedResult = BigDecimal.valueOf(15);

    HashMap<Integer, List<BigDecimal>> simResults = new HashMap<>();
    simResults.put(0, Lists.newArrayList(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(20)));
    simResults.put(1, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), expectedResult));
    simResults.put(2, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(5)));

    SimulationResult simulationResult = SimulationResult.builder()
      .projectionResults(simResults)
      .build();

    BigDecimal median = monteCarloStatisticsGenerator.calcPercentile(0.50, 3, simulationResult);

    assertNotNull(median);
    assertThat(median, is(expectedResult));
  }

  @Test
  public void shouldCalculateMedianSuccessfullyGivenEvenBigDecimalArray() throws InvalidStatsGenerationParamsException {
    HashMap<Integer, List<BigDecimal>> simResults = new HashMap<>();
    simResults.put(0, Lists.newArrayList(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(20)));
    simResults.put(1, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(15)));
    simResults.put(2, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(5)));
    simResults.put(3, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(25)));

    SimulationResult simulationResult = SimulationResult.builder()
      .projectionResults(simResults)
      .build();

    BigDecimal median = monteCarloStatisticsGenerator.calcPercentile(0.50, 3, simulationResult);

    assertNotNull(median);
    assertThat(median, is(BigDecimal.valueOf(15)));
  }

  @Test
  public void shouldCalculate90thPercentileSuccessfullyGivenBigDecimalArray() throws InvalidStatsGenerationParamsException {
    double ninetiethPercentile = 0.90;

    HashMap<Integer, List<BigDecimal>> simResults = new HashMap<>();
    simResults.put(0, Lists.newArrayList(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(20)));
    simResults.put(1, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(15)));
    simResults.put(2, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(5)));
    simResults.put(3, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(25)));

    SimulationResult simulationResult = SimulationResult.builder()
      .projectionResults(simResults)
      .build();

    BigDecimal bestCase = monteCarloStatisticsGenerator.calcPercentile(ninetiethPercentile, 3, simulationResult);

    assertNotNull(bestCase);
    assertThat(bestCase, is(BigDecimal.valueOf(25)));
  }

  @Test
  public void shouldCalculate10thPercentileSuccessfullyGivenBigDecimalArray() throws InvalidStatsGenerationParamsException {
    double tenthPercentile = 0.10;

    HashMap<Integer, List<BigDecimal>> simResults = new HashMap<>();
    simResults.put(0, Lists.newArrayList(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(20)));
    simResults.put(1, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(15)));
    simResults.put(2, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(5)));
    simResults.put(3, Lists.newArrayList(BigDecimal.ONE, BigDecimal.valueOf(5), BigDecimal.valueOf(25)));

    SimulationResult simulationResult = SimulationResult.builder()
      .projectionResults(simResults)
      .build();

    BigDecimal bestCase = monteCarloStatisticsGenerator.calcPercentile(tenthPercentile, 3, simulationResult);

    assertNotNull(bestCase);
    assertThat(bestCase, is(BigDecimal.valueOf(5)));
  }


  @SpringBootApplication
  static class TestConfiguration {
  }
}
