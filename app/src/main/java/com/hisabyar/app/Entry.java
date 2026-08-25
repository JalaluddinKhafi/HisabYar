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
        this.date=Dates.validate(date);
    }
    public org.json.JSONObject toJson() throws org.json.JSONException {
        org.json.JSONObject o=new org.json.JSONObject();
        o.put("id",id); o.put("type",type); o.put("amount",amount.toPlainString());
        o.put("currency",currency); o.put("category",category); o.put("source",source);
        o.put("note",note); o.put("date",date); return o;
    }
    public static Entry fromJson(org.json.JSONObject o) throws org.json.JSONException {
        return new Entry(o.get("id").toString(), o.getString("type"), decimalText(o.get("amount")),
            o.getString("currency"), o.getString("category"), o.optString("source",""),
            o.optString("note",""), o.getString("date"));
    }
    private static String decimalText(Object value) {
        if(!(value instanceof Number)) return value.toString();
        java.math.BigDecimal number=new java.math.BigDecimal(value.toString());
        if(number.signum()<=0 || number.compareTo(new java.math.BigDecimal("999999999999.99"))>0 || number.stripTrailingZeros().scale()>2)
            throw new IllegalArgumentException("مبلغ قدیمی خارج از محدوده یا دارای اعشار نامعتبر است.");
        return number.setScale(2,java.math.RoundingMode.UNNECESSARY).toPlainString();
    }
    private static String text(String text, int limit) {
        String value=text == null ? "" : text.trim();
        if (value.length()>limit) throw new IllegalArgumentException("متن بیش از حد طولانی است.");
        return value;
    }
    public static String newId() { return UUID.randomUUID().toString(); }
}
