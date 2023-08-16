package com.server.calculator;

import com.server.calculator.operators.*;

import java.util.List;

public class Calculator {
    private static final List<ArithmeticOperator> arithmeticOperators =
            List.of(new AdditionOperator(), new DivisionOperator(),
                    new MultiplicationOperator(), new SubtractionOperator());

    public static int calculate(PositiveNumber num1, String operator, PositiveNumber num2) {
        return arithmeticOperators.stream()
                .filter(arithmeticOperator -> arithmeticOperator.supports(operator))
                .map(arithmeticOperator -> arithmeticOperator.calculate(num1, num2))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Please provide a correct arithmetic."));
    }

}
