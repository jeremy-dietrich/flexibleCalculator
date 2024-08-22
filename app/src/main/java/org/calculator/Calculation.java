package org.calculator;

import java.math.BigDecimal;

/**
 * The Calculation interface defines methods to chain arithmetic operations.
 */
public interface Calculation {
  /**
   * The addOperation method adds an operation to apply to the current value of the calculation. The first operand
   * is the current value of the calculation and the second operand is the num parameter.
   *
   * @param op Operation that defines what arithmetic operation to apply between the current value and num.
   * @param num The second operand.
   * @return Calculation representing the current calculation. This can be used to chain operations.
   */
  Calculation addOperation(Operation op, Number num);

  /**
   * The getValue method returns the current value of the calculation as a BigDecimal.
   *
   * @return BigDecimal representing the current value of the calculation.
   */
  BigDecimal getValue();
}
