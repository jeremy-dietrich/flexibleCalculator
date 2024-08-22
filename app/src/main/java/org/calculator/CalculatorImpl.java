package org.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.atomic.*;

/**
 * The CalculatorImpl class is an implementation of the {@link org.calculator.Calculator} interface. It uses BigDecimal
 * as the underlying numeric data type, but accepts various Number types as parameters including AtomicInteger,
 * AtomicLong, BigDecimal, BigInteger, Byte, Double, DoubleAccumulator, DoubleAdder, Float, Integer, Long,
 * LongAccumulator, LongAdder, and Short. The Number parameters are converted to BigDecimal before applying arithmetic
 * operations. The CalculatorImpl class does not support atomic functionality even if atomic Number types are
 * passed as parameters.
 */
public class CalculatorImpl implements Calculator {
  private static final int SCALE_DEFAULT_VALUE = 10;

  private class CalculationImpl implements Calculation {
    private BigDecimal value;

    private CalculationImpl(Number num) {
      value = convertToBigDecimal(num);
    }

    @Override
    public Calculation addOperation(Operation op, Number num) {
      value = calculate(op, value, num);
      return this;
    }

    @Override
    public BigDecimal getValue() {
      return value;
    }
  }

  private final int scale;

  public CalculatorImpl(int scale) {
    this.scale = scale;
  }

  public CalculatorImpl() {
    this.scale = SCALE_DEFAULT_VALUE;
  }

  @Override
  public BigDecimal calculate(Operation op, Number num1, Number num2) {
    if (op == null) {
      throw new NullPointerException("op must not be null.");
    }

    BigDecimal bigDecimal1 = convertToBigDecimal(num1);
    BigDecimal bigDecimal2 = convertToBigDecimal(num2);
    return op.apply(bigDecimal1, bigDecimal2);
  }

  @Override
  public Calculation startCalculation(Number num) {
    return new CalculationImpl(num);
  }

  protected BigDecimal convertToBigDecimal(Number num) {
    if (num instanceof Long || num instanceof Integer || num instanceof Short || num instanceof Byte
        || num instanceof AtomicLong || num instanceof AtomicInteger || num instanceof LongAccumulator
        || num instanceof LongAdder) {
      return BigDecimal.valueOf(num.longValue()).setScale(scale, RoundingMode.HALF_EVEN);
    } else if (num instanceof Double || num instanceof Float || num instanceof DoubleAccumulator
        || num instanceof DoubleAdder) {
      return BigDecimal.valueOf(num.doubleValue()).setScale(scale, RoundingMode.HALF_EVEN);
    } else if (num instanceof BigDecimal) {
      return ((BigDecimal) num).setScale(scale, RoundingMode.HALF_EVEN);
    } else if (num instanceof BigInteger) {
      return new BigDecimal((BigInteger) num).setScale(scale, RoundingMode.HALF_EVEN);
    } else if (num == null) {
      throw new NullPointerException("The num parameter must not be null.");
    } else {
      throw new UnsupportedOperationException(
          String.format("%s is not a supported operand type.", num.getClass().getCanonicalName()));
    }
  }
}
