package com.sohamkamani;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorTest {

    @Test
    public void ShouldAdd() {
        assertEquals(2, Calculator.add(1, 1));
    }
}
