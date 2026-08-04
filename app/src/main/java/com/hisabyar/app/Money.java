package com.hisabyar.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Money {
    private Money() {}
    public static BigDecimal parse(String text) {
        text = normalize(text);
        if (!text.trim().matches("[0-9]+(\\.[0-9]{1,2})?")) {
            throw new IllegalArgumentException("مبلغ را با حداکثر دو رقم اعشار وارد کنید.");
        }
        BigDecimal value = new BigDecimal(text.trim()).setScale(2, RoundingMode.UNNECESSARY);
        if (value.signum() <= 0 || value.compareTo(new BigDecimal("999999999999.99")) > 0) {
            throw new IllegalArgumentException("مبلغ باید بین ۰٫۰۱ و ۹۹۹٬۹۹۹٬۹۹۹٬۹۹۹٫۹۹ باشد.");
        }
        return value;
    }
    public static String normalize(String text) {
        if (text == null) return "";
        StringBuilder normalized = new StringBuilder();
        for (char c : text.trim().toCharArray()) {
            if (c >= '۰' && c <= '۹') c = (char) ('0' + c - '۰');
            else if (c >= '٠' && c <= '٩') c = (char) ('0' + c - '٠');
            else if (c == '٫') c = '.';
            normalized.append(c);
        }
        return normalized.toString();
    }
    public static String format(BigDecimal value) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }
}
