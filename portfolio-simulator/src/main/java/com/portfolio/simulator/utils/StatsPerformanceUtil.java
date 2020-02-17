package com.portfolio.simulator.utils;

import java.math.BigDecimal;
import java.util.Arrays;

public final class StatsPerformanceUtil {

  public static BigDecimal derivePercentile(BigDecimal[] values, double percentile) {
    Arrays.sort(values);
    int index = (int) Math.ceil(values.length * percentile);
    return values[index - 1];
  }
}
