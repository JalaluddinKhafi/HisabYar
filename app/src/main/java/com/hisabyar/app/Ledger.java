package com.hisabyar.app;
import java.math.BigDecimal;
import java.util.List;
public final class Ledger {
    private Ledger() {}
    public static BigDecimal sum(List<Entry> entries,String type,String currency,String prefix) {
        BigDecimal total=new BigDecimal("0.00");
        for(Entry entry:entries) {
            if(type!=null && !type.equals(entry.type)) continue;
            if(currency!=null && !currency.equals(entry.currency)) continue;
            if(prefix!=null && !entry.date.startsWith(prefix)) continue;
            total=total.add(entry.amount);
        }
        return total;
    }
    public static BigDecimal balance(List<Entry> entries,String currency,String prefix) {
        return sum(entries,"income",currency,prefix).subtract(sum(entries,"expense",currency,prefix));
    }
}
