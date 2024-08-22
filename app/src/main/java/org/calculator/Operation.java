package org.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

/**
 * The Operation enum represents arithmetic operations.
 */
public enum Operation {
  ADD(BigDecimal::add),
  SUBTRACT(BigDecimal::subtract),
  MULTIPLY(BigDecimal::multiply),
  DIVIDE((a, b) -> a.divide(b, RoundingMode.HALF_EVEN));

  private final BiFunction<BigDecimal, BigDecimal, BigDecimal> operation;

  Operation(BiFunction<BigDecimal, BigDecimal, BigDecimal> operation) {
    this.operation = operation;
  }

  public BigDecimal apply(BigDecimal num1, BigDecimal num2) {
    return this.operation.apply(num1, num2);
  }
}
