package com.hisabyar.app;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
public final class EntryFilter {
    private EntryFilter() {}
    public static List<Entry> select(List<Entry> entries,String currency,String type,String prefix,String query) {
        String needle=query==null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<Entry> result=new ArrayList<>();
        for(Entry e:entries) {
            if(currency!=null && !currency.equals(e.currency)) continue;
            if(type!=null && !type.equals(e.type)) continue;
            if(prefix!=null && !e.date.startsWith(prefix)) continue;
            if(!needle.isEmpty() && !(e.source+" "+e.note+" "+e.category+" "+e.date+" "+e.amount.toPlainString()).toLowerCase(Locale.ROOT).contains(needle)) continue;
            result.add(e);
        }
        // Stable sort preserves insertion order for equal dates; newest insertion first.
        java.util.Collections.reverse(result);
        result.sort(Comparator.comparing((Entry e)->e.date).reversed());
        return result;
    }
}
