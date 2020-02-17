package com.portfolio.simulator.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PortfolioTest {

  @Test
  public void shouldCreateAggressivePortfolioSuccessfully() {
    BigDecimal balance = BigDecimal.valueOf(100.00);

    Portfolio aggressivePortfolio = new Portfolio(PortfolioType.AGGRESSIVE, balance);

    assertNotNull(aggressivePortfolio);
    assertThat(aggressivePortfolio.getType(), is(PortfolioType.AGGRESSIVE));
    assertThat(aggressivePortfolio.getBalance(), is(balance));
    assertNotNull(aggressivePortfolio.getTargetAllocation());
    assertThat(aggressivePortfolio.getTargetAllocation().getHistoricalReturn(), is(BigDecimal.valueOf(0.094324)));
    assertThat(aggressivePortfolio.getTargetAllocation().getHistoricalRisk(), is(BigDecimal.valueOf(0.15675)));
  }

  @Test
  public void shouldCreateVeryConservativePortfolioSuccessfully() {
    BigDecimal balance = BigDecimal.valueOf(50.00);

    Portfolio veryConservativePortfolio = new Portfolio(PortfolioType.VERY_CONSERVATIVE, balance);

    assertNotNull(veryConservativePortfolio);
    assertThat(veryConservativePortfolio.getType(), is(PortfolioType.VERY_CONSERVATIVE));
    assertThat(veryConservativePortfolio.getBalance(), is(balance));
    assertNotNull(veryConservativePortfolio.getTargetAllocation());
    assertThat(veryConservativePortfolio.getTargetAllocation().getHistoricalReturn(), is(BigDecimal.valueOf(0.06189)));
    assertThat(veryConservativePortfolio.getTargetAllocation().getHistoricalRisk(), is(BigDecimal.valueOf(0.063438)));
  }

  @Test
  public void shouldThrowAnExceptionWhenCreatingPortfolioWithInvalidType() {
    BigDecimal balance = BigDecimal.valueOf(0.00);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Portfolio(PortfolioType.valueOf("DEFAULT"), balance);
    });

    assertThat(exception.getMessage(),
      is("No enum constant com.portfolio.simulator.domain.PortfolioType.DEFAULT"));
  }

  @SpringBootApplication
  static class TestConfiguration {
  }
}
