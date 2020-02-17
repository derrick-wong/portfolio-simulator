package com.portfolio.simulator.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ResultsUtil {

  public static BigDecimal[] retrieveResultsByYear(HashMap<Integer, List<BigDecimal>> simulationResults,
                                                   Integer year) {
    List<BigDecimal> filteredValues = new ArrayList<>();
    simulationResults
      .forEach(
        (sim, result) -> {
          filteredValues.add(result.get(year - 1));
        });
    return filteredValues.toArray(new BigDecimal[simulationResults.size()]);
  }
}
