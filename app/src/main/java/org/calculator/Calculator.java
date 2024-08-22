package org.calculator;

import java.math.BigDecimal;

/**
 * The Calculator interface defines methods that apply operations to numbers and return the results as a BigDecimal.
 */
public interface Calculator {
  /**
   * The calculate method applies an operation to the num1 and num2 parameters and returns the result as a BigDecimal.
   *
   * @param op Operation that defines what arithmetic operation to apply to num1 and num2.
   * @param num1 The first operand.
   * @param num2 The second operand.
   * @return BigDecimal value representing the result of applying the operation to the two operands.
   */
  BigDecimal calculate(Operation op, Number num1, Number num2);

  /**
   * The startCalculation method returns a new Calculation that can be used to chain applying operations to operands.
   *
   * @param num The original value of the calculation.
   * @return Calculation representing the current state of the calculation.
   */
  Calculation startCalculation(Number num);
}
