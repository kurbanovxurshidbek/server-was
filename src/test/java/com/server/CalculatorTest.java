package com.server;

import com.server.calculator.Calculator;
import com.server.calculator.operators.PositiveNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTest {

    @DisplayName("First test")
    @Test
    void calculateTest(){
        int result = Calculator.calculate(new PositiveNumber(1),"+", new PositiveNumber(2));
        assertThat(result).isEqualTo(3);
    }

}
