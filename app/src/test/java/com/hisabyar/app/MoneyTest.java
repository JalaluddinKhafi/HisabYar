package com.hisabyar.app;
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
public class MoneyTest {
    @Test public void decimalAdditionIsExact() {
        assertEquals(new BigDecimal("0.30"), Money.parse("0.10").add(Money.parse("0.20")));
    }
    @Test public void invalidAmountsAreRejected() {
        for (String text : new String[]{"0", "-1", "NaN", "Infinity", "1e3", "1.001", "1000000000000", "", ".5"}) {
            try { Money.parse(text); fail(text); } catch (IllegalArgumentException expected) {}
        }
    }
    @Test public void formatKeepsCents() { assertEquals("1,234.50", Money.format(Money.parse("1234.5"))); }
}
