package org.calculator;

import java.math.BigDecimal;

public interface Calculation {
  Calculation addOperation(Operation op, Number num);

  BigDecimal getValue();
}
