Flexible Calculator

Description:
The purpose of this project is to provide a Calculator class that can apply various 
arithmetic operation on Numbers. The available operations include ADD, SUBTRACT, 
MULTIPLY, and DIVIDE.

Assumptions:
- The calculate method should work with various Number types.
- It is not necessary to perform operations using the Number types passed to
the calculate method.
- Atomic functionality for Number types such as AtomicInteger does not need to
be supported.

Design Decisions:
- Use Java 8.
- Use BigDecimal as underlying numerical data type and convert other Number types
to BigDecimal before applying operations. This is because BigDecimal
can represent other Number data types accurately. Also, this simplifies the
code because the implementation of arithmetic operations does not need to change
depending on the Number type of the operands.
- Support the Number data types listed in the Java 8 Javadoc for the Number class
as Direct Known Subclasses. These include AtomicInteger, AtomicLong, BigDecimal, BigInteger, Byte, Double, 
DoubleAccumulator, DoubleAdder, Float, Integer, Long, LongAccumulator, LongAdder, and Short. Other Number 
types such as AtomicDouble could be supported by subclassing the CalculatorImpl class and overriding the
convertToBigDecimal method.
- Put the operation implementation in the Operation enum. The operation 
implementation is passed as a BiFunction that takes two BigDecimal parameters 
and returns a BigDecimal result. This way new operations can be added easily by
updating the Operation enum. It also prevents unknown operations because the Operation enum
requires an operation implementation passed to its constructor, which means every Operation enum value
should have an operation implementation.
