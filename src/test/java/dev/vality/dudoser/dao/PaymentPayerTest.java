package dev.vality.dudoser.dao;

import dev.vality.dudoser.dao.model.PaymentPayer;
import dev.vality.dudoser.utils.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentPayerTest {

    PaymentPayer paymentPayer;

    @BeforeEach
    public void setUp() {
        paymentPayer = PaymentPayer.builder().build();
    }

    @Test
    public void testGetAmountWithCurrency() throws Exception {
        paymentPayer.setAmount(Converter.longToBigDecimal(12345L));
        paymentPayer.setCurrency("RUB");
        assertEquals("123.45 RUB", Converter.getFormattedAmount(paymentPayer.getAmount(), paymentPayer.getCurrency()));

        paymentPayer.setAmount(Converter.longToBigDecimal(12145345L));
        paymentPayer.setCurrency("USD");
        assertEquals("121453.45 USD",
                Converter.getFormattedAmount(paymentPayer.getAmount(), paymentPayer.getCurrency()));

        paymentPayer.setAmount(Converter.longToBigDecimal(0L));
        paymentPayer.setCurrency("BYR");
        assertEquals("0.00 BYR", Converter.getFormattedAmount(paymentPayer.getAmount(), paymentPayer.getCurrency()));

        paymentPayer.setAmount(Converter.longToBigDecimal(-1L));
        paymentPayer.setCurrency("FRA");
        assertEquals("-0.01 FRA", Converter.getFormattedAmount(paymentPayer.getAmount(), paymentPayer.getCurrency()));
    }
}
