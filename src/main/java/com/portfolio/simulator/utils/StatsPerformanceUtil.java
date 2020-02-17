package com.portfolio.simulator.utils;

import java.math.BigDecimal;
import java.util.Arrays;

public final class StatsPerformanceUtil {

  public static BigDecimal derivePercentile(BigDecimal[] values, double percentile) {
    Arrays.sort(values);
    double index = values.length * percentile;
    return values[(int) Math.ceil(index) - 1];
  }
}
