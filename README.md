# Modern Portfolio Theory​ ​

Modern Portfolio Theory says that it is not enough to look at the expected risk and return of one particular stock. By investing in more than one stock, an investor can reap the benefits of diversification- chief among them, a reduction in the riskiness of the portfolio.

A Real-Time implementation of Modern Portfolio Theory is to build an optimal asset allocation of domestic stocks & bonds, international stock, and bonds, alternatives, and cash. Combining the risk & return of each asset class and correlations across them – we come up with the risk and return of the overall portfolio. Two portfolios were created, one being extremely Conservative and one being Aggressive.

| Portfolio Type | Return (Mean) | Risk ( Standard Deviation) |
|----------------|---------------|----------------------------|
| Aggressive     | %9.4324       | 15.675                     |
| Very Conservative | %6.189     | 6.3438                     |
 
We would now like to compare their performance against each other. We would like to know that if a user with $100,000 invested their money in either of them, how would the returns compare over the next 20 years. We would like to test their performance by using forward-looking Monte Carlo Simulations.

## Monte Carlo Simulation:
This is a statistical technique that uses pseudo-random uniform variables for a given statistical distribution based on past risk (SD) and return (mean) to predict outcomes over future time periods. Based on the iterative evaluation of each random future value, we project the portfolio future value over 20 years. We would like to run 10,000 simulations of projecting 20-year value and come up with the following:

### Assumptions
1.​ ​We would like to use a random number generator to ensure the Gaussian distribution of random numbers that are generated.
th​
2.​ ​20​ Year future value should be inflation adjusted at the rate of 3.5% each year. Ie. Year 1
value of 103.5 is equivalent to 100 at Year 0.

### Solution Needed
1.​ ​Portfolio Inflation Adjusted Values from Simulation

| Portfolio Type | Median 20th​ Year | 10 % Best Case | 10 % Worst Case |
|----------------|------------------|----------------|-----------------|
| A - Aggressive |                  |                |                 |
| I - Very Conservative |           |                |                 |


### Description
10% Best Case: 90​ Percentile value among the 10,000 simulations 
10% Worst Case: 10​ Percentile value among the 10,000 simulations.

# Project Setup
## IntelliJ
Run following command in Terminal before importing.
```
./gradlew cleanIdea idea
```
Import project using `build.gradle`.

## Compile, Test & Build
Run following command in Terminal (below commands are using log-level: INFO).
```
./gradlew --info
```
or
```
./gradlew clean test build --info
```

### Artifact Details
The `portfolio-simulator` default generated artifact path is located at `$ROOT_PROJ/build/libs/` with artifact-name as `portfolio-simulator-${version}.jar`.

### Artifact Usage Details
To use locally generated artifact, copy artifact to the project's destination folder (e.g.: `$OTHER_PROJ/libs/`), then add the following dependency via Gradle:
```
dependencies {
    implementation files("/libs/portfolio-simulator-${version}.jar")
}
```

