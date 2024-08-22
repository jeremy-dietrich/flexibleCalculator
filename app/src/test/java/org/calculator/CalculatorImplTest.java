package org.calculator;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.atomic.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorImplTest {
  private final Calculator calculator = new CalculatorImpl();

  private static Stream<Arguments> getTestParameters() {
    return Stream.of(
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, (byte) 3, (byte) 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, (short) 3, (short) 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, 3, 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, (long) 3, (long) 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, new AtomicInteger(3), (long) 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, new AtomicLong(3), (long) 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, new LongAccumulator(Long::sum, 3), (long) 9),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, new LongAdder(), (long) 12),
        Arguments.of(BigDecimal.valueOf(12), 10, Operation.ADD, BigInteger.valueOf(3), (long) 9),
        Arguments.of(BigDecimal.valueOf(1.3), 4, Operation.ADD, (float) 1.1, (float) 0.2),
        Arguments.of(BigDecimal.valueOf(1.3), 4, Operation.ADD, 1.1, 0.2),
        Arguments.of(BigDecimal.valueOf(1.3), 4, Operation.ADD, new DoubleAccumulator(Double::sum, 1.1), 0.2),
        Arguments.of(BigDecimal.valueOf(1.3), 4, Operation.ADD, new DoubleAdder(), 1.3),
        Arguments.of(BigDecimal.valueOf(1.3), 4, Operation.ADD, BigDecimal.valueOf(1.1), 0.2),
        Arguments.of(BigDecimal.valueOf(Integer.MAX_VALUE).multiply(BigDecimal.valueOf(2)), 10,
            Operation.ADD, Integer.MAX_VALUE, Integer.MAX_VALUE),
        Arguments.of(BigDecimal.valueOf(Long.MAX_VALUE).multiply(BigDecimal.valueOf(2)), 10,
            Operation.ADD, Long.MAX_VALUE, Long.MAX_VALUE),
        Arguments.of(BigDecimal.valueOf(-6), 10, Operation.SUBTRACT, (byte) 3, (byte) 9),
        Arguments.of(BigDecimal.valueOf(-6), 10, Operation.SUBTRACT, (short) 3, (short) 9),
        Arguments.of(BigDecimal.valueOf(-6), 10, Operation.SUBTRACT, 3, 9),
        Arguments.of(BigDecimal.valueOf(-6), 10, Operation.SUBTRACT, (long) 3, (long) 9),
        Arguments.of(BigDecimal.valueOf(-6), 10, Operation.SUBTRACT, (float) 3, (float) 9),
        Arguments.of(BigDecimal.valueOf(-6), 10, Operation.SUBTRACT, (double) 3, (double) 9),
        Arguments.of(BigDecimal.valueOf(Long.MIN_VALUE).subtract(BigDecimal.valueOf(Long.MAX_VALUE)), 10,
            Operation.SUBTRACT, Long.MIN_VALUE, Long.MAX_VALUE),
        Arguments.of(BigDecimal.valueOf(27), 10, Operation.MULTIPLY, (byte) 3, (byte) 9),
        Arguments.of(BigDecimal.valueOf(27), 10, Operation.MULTIPLY, (short) 3, (short) 9),
        Arguments.of(BigDecimal.valueOf(27), 10, Operation.MULTIPLY, 3, 9),
        Arguments.of(BigDecimal.valueOf(27), 10, Operation.MULTIPLY, (long) 3, (long) 9),
        Arguments.of(BigDecimal.valueOf(27), 10, Operation.MULTIPLY, (float) 3, (float) 9),
        Arguments.of(BigDecimal.valueOf(27), 10, Operation.MULTIPLY, (double) 3, (double) 9),
        Arguments.of(BigDecimal.valueOf(Long.MAX_VALUE).multiply(BigDecimal.valueOf(Long.MAX_VALUE)), 10,
            Operation.MULTIPLY, Long.MAX_VALUE, Long.MAX_VALUE),
        Arguments.of(BigDecimal.valueOf(5), 10, Operation.DIVIDE, (byte) 10, (byte) 2),
        Arguments.of(BigDecimal.valueOf(5), 10, Operation.DIVIDE, (short) 10, (short) 2),
        Arguments.of(BigDecimal.valueOf(5), 10, Operation.DIVIDE, 10, 2),
        Arguments.of(BigDecimal.valueOf(5), 10, Operation.DIVIDE, (long) 10, (long) 2),
        Arguments.of(BigDecimal.valueOf(5), 10, Operation.DIVIDE, (float) 10, (float) 2),
        Arguments.of(BigDecimal.valueOf(5), 10, Operation.DIVIDE, (double) 10, (double) 2),
        Arguments.of(BigDecimal.valueOf(0.3333333333), 10, Operation.DIVIDE, 1, 3),
        Arguments.of(BigDecimal.valueOf(0.3333333333), 10, Operation.DIVIDE, 1.0, 3.0),
        Arguments.of(BigDecimal.valueOf(1).setScale(50, RoundingMode.HALF_EVEN)
                .divide(BigDecimal.valueOf(Long.MAX_VALUE), RoundingMode.HALF_EVEN),
            50, Operation.DIVIDE, 1, Long.MAX_VALUE));
  }

  @ParameterizedTest
  @MethodSource("getTestParameters")
  void calculate(BigDecimal expected, int scale, Operation op, Number num1, Number num2) {
    Calculator calculator = new CalculatorImpl(scale);
    assertEquals(0, expected.compareTo(calculator.calculate(op, num1, num2)));
  }

  @Test()
  void calculateWithNull() {
    assertThrows(NullPointerException.class, () -> calculator.calculate(Operation.ADD, null, null));
  }

  @Test()
  void calculateWithNullOperation() {
    assertThrows(NullPointerException.class, () -> calculator.calculate(null, 1, 1));
  }

  @Test()
  void calculateWithDivideByZero() {
    assertThrows(ArithmeticException.class, () -> calculator.calculate(Operation.DIVIDE, 1, 0));
  }

  @Test()
  void calculateWithUnsupportedNumberType() {
    assertThrows(UnsupportedOperationException.class, () -> calculator.calculate(Operation.DIVIDE, new AtomicDouble(4.0), 2.0));
  }

  @Test
  void startCalculation() {
    assertEquals(0, BigDecimal.valueOf(1).compareTo(calculator.startCalculation(1).getValue()));
    assertEquals(0, BigDecimal.valueOf(1).compareTo(calculator.startCalculation(1)
        .addOperation(Operation.SUBTRACT, 1).addOperation(Operation.ADD, 1)
        .addOperation(Operation.MULTIPLY, 1).addOperation(Operation.DIVIDE, 1).getValue()));
    assertEquals(0, BigDecimal.valueOf(1).compareTo(calculator.startCalculation(1.0)
        .addOperation(Operation.SUBTRACT, 1.0).addOperation(Operation.ADD, 1.0)
        .addOperation(Operation.MULTIPLY, 1.0).addOperation(Operation.DIVIDE, 1.0).getValue()));
    Calculation calculation = calculator.startCalculation(11);
    assertEquals(0, BigDecimal.valueOf(11).compareTo(calculation.getValue()));
    calculation.addOperation(Operation.ADD, 1.5);
    assertEquals(0, BigDecimal.valueOf(12.5).compareTo(calculation.getValue()));
    calculation.addOperation(Operation.SUBTRACT, (float) 2.5).addOperation(Operation.MULTIPLY, (long) 5);
    assertEquals(0, BigDecimal.valueOf(50).compareTo(calculation.getValue()));
  }
}