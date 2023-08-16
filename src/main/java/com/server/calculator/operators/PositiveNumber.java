package com.server.calculator.operators;

public class PositiveNumber {
    private static final String ZERO_OR_NEGATIVE_NUMBER_EXCEPTION_MESSAGE = "Cannot pass 0 or negative numbers";

    private int value;

    public PositiveNumber(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (isNegativeNumber(value)) {
            throw new IllegalArgumentException(ZERO_OR_NEGATIVE_NUMBER_EXCEPTION_MESSAGE);
        }
    }

    private boolean isNegativeNumber(int number) {
        return number <= 0;
    }

    public int toInt() {
        return value;
    }
}
