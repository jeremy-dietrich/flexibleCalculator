package org.calculator;

import java.math.BigDecimal;

public interface Calculator {
  BigDecimal calculate(Operation op, Number num1, Number num2);

  Calculation startCalculation(Number num);
}
