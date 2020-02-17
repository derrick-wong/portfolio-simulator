package com.portfolio.simulator.service;

import com.portfolio.simulator.domain.Portfolio;
import com.portfolio.simulator.domain.SimulationParams;
import com.portfolio.simulator.domain.SimulationResult;
import com.portfolio.simulator.exception.InvalidSimulationParamsException;

public interface Simulator {
  SimulationResult run(Portfolio portfolio, SimulationParams params) throws InvalidSimulationParamsException;
}
