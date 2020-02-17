package com.portfolio.simulator.service;

import com.portfolio.simulator.exception.InvalidStatsGenerationParamsException;

public interface StatsGenerator<T, K> {
  T calcPercentile(double percent, int year, K simulationResult) throws InvalidStatsGenerationParamsException;
}
