package dev.vality.dudoser.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest {

    @Test
    public void longToBigDecimal() throws Exception {
        assertEquals(new BigDecimal("0.00"), Converter.longToBigDecimal(0L));
        assertEquals(new BigDecimal("122.55"), Converter.longToBigDecimal(12255L));
        assertEquals(new BigDecimal("1225.54"), Converter.longToBigDecimal(122554L));
        assertEquals(new BigDecimal("-0.01"), Converter.longToBigDecimal(-1L));
    }
}
