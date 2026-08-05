package com.hisabyar.app;

import java.math.BigDecimal;
import java.util.UUID;

public final class Entry {
    public final String id, type, currency, category, source, note, date;
    public final BigDecimal amount;
    public Entry(String id, String type, String amount, String currency, String category, String source, String note, String date) {
        if (id == null || id.trim().isEmpty() || id.length() > 128) throw new IllegalArgumentException("شناسه نامعتبر است.");
        if (!"expense".equals(type) && !"income".equals(type)) throw new IllegalArgumentException("نوع ثبت نامعتبر است.");
        if (!"AFN".equals(currency) && !"USD".equals(currency)) throw new IllegalArgumentException("ارز نامعتبر است.");
        this.id=id; this.type=type; this.amount=Money.parse(amount); this.currency=currency;
        this.category=text(category, 80); this.source=text(source, 200); this.note=text(note, 1000);
        if (this.category.isEmpty()) throw new IllegalArgumentException("دسته‌بندی الزامی است.");
        this.date=date;
    }
    private static String text(String text, int limit) {
        String value=text == null ? "" : text.trim();
        if (value.length()>limit) throw new IllegalArgumentException("متن بیش از حد طولانی است.");
        return value;
    }
    public static String newId() { return UUID.randomUUID().toString(); }
}
