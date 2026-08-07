package com.hisabyar.app;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
public class LedgerTest {
    static Entry e(String id,String type,String amount,String currency,String date) {
        return new Entry(id,type,amount,currency,"سایر","","",date);
    }
    final List<Entry> entries=Arrays.asList(e("1","income","0.1","AFN","2026-08-02"),
        e("2","income","0.2","AFN","2026-08-03"),e("3","expense","1","AFN","2026-08-02"),
        e("4","income","99","USD","2026-08-02"),e("5","income","100","AFN","2026-09-01"));
    @Test public void monthlyBalanceCanBeNegative() { assertEquals("-0.70",Ledger.balance(entries,"AFN","2026-08").toPlainString()); }
    @Test public void dayAndCurrencyAreIndependent() { assertEquals("99.00",Ledger.sum(entries,"income","USD","2026-08-02").toPlainString()); }
    @Test public void emptyTotalsHaveTwoDecimals() { assertEquals("0.00",Ledger.balance(entries,"USD","2025").toPlainString()); }
}
