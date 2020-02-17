Business Need: Generate Portfolio Inflation Adjusted Values from Simulation for Median, 10% Best Case, and 10% Worst Case

  Scenario: Monte Carlo Simulation to Project Aggressive Portfolio Inflation Adjusted Values
    Given A user has created an 'AGGRESSIVE' portfolio with principal of 100000.00 dollars
    And The market has an inflationRate of 3.5% each year
    When The user executes a Monte Carlo Simulation with 10000 simulations to project future value over 20 years
    Then The user should be able to see the Median value for year 20
    And The user should be able to see the 90th percentile for year 20
    And The user should be able to see the 10th percentile for year 20

  Scenario: Monte Carlo Simulation to Project Very Conservative Portfolio Inflation Adjusted Values
    Given A user has created an 'VERY_CONSERVATIVE' portfolio with principal of 100000.00 dollars
    And The market has an inflationRate of 3.5% each year
    When The user executes a Monte Carlo Simulation with 10000 simulations to project future value over 20 years
    Then The user should be able to see the Median value for year 20
    And The user should be able to see the 90th percentile for year 20
    And The user should be able to see the 10th percentile for year 20
