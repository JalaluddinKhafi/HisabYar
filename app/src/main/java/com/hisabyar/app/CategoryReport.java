package com.hisabyar.app;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
public final class CategoryReport {
    private CategoryReport() {}
    public static String expenses(java.util.List<Entry> entries,String prefix) {
        StringBuilder text=new StringBuilder();
        for(String currency:new String[]{"AFN","USD"}) {
            text.append(currency).append("\n"); Map<String,BigDecimal> totals=new TreeMap<>();
            for(Entry e:entries) if(e.type.equals("expense") && e.currency.equals(currency) && e.date.startsWith(prefix)) {
                BigDecimal previous=totals.get(e.category);
                totals.put(e.category,(previous==null?new BigDecimal("0.00"):previous).add(e.amount));
            }
            if(totals.isEmpty()) text.append("مصرفی ثبت نشده است.\n");
            for(Map.Entry<String,BigDecimal> row:totals.entrySet()) text.append(row.getKey()).append(": ").append(Money.format(row.getValue())).append("\n");
        }
        return text.toString();
    }
}
