package com.hisabyar.app;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public final class Dates {
    private Dates() {}
    public static String today() { return new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date()); }
    public static String validate(String date) {
        if (date==null || !date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) throw new IllegalArgumentException("تاریخ میلادی نامعتبر است.");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        format.setLenient(false); ParsePosition position=new ParsePosition(0);
        Date parsed=format.parse(date,position);
        if (parsed==null || position.getIndex()!=date.length() || !format.format(parsed).equals(date)) throw new IllegalArgumentException("تاریخ میلادی نامعتبر است.");
        return date;
    }
}
