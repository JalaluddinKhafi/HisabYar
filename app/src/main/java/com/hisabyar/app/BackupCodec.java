package com.hisabyar.app;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public final class BackupCodec {
    public static final int MAX_ENTRIES=10000;
    public static final int MAX_BYTES=8*1024*1024;
    private BackupCodec() {}
    public static List<Entry> decode(String text) throws JSONException {
        if(text==null || text.length()>MAX_BYTES || text.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>MAX_BYTES) throw new IllegalArgumentException("فایل پشتیبان بیش از حد بزرگ است.");
        String raw=text.trim();
        if(raw.startsWith("\uFEFF")) raw=raw.substring(1).trim();
        JSONArray rows;
        if(raw.startsWith("[")) rows=new JSONArray(raw);
        else {
            JSONObject envelope=new JSONObject(raw);
            if(!"HisabYar".equals(envelope.getString("app")) || !Integer.valueOf(1).equals(envelope.get("version")))
                throw new IllegalArgumentException("قالب پشتیبان پشتیبانی نمی‌شود.");
            rows=envelope.getJSONArray("entries");
        }
        if(rows.length()>MAX_ENTRIES) throw new IllegalArgumentException("حداکثر ۱۰۰۰۰ ثبت پشتیبانی می‌شود.");
        List<Entry> entries=new ArrayList<>(); Set<String> ids=new HashSet<>();
        for(int i=0;i<rows.length();i++) {
            Entry e=Entry.fromJson(rows.getJSONObject(i));
            if(!ids.add(e.id)) throw new IllegalArgumentException("شناسهٔ تکراری در پشتیبان وجود دارد.");
            entries.add(e);
        }
        return entries;
    }
    public static String encode(List<Entry> entries) throws JSONException {
        if(entries.size()>MAX_ENTRIES) throw new IllegalArgumentException("تعداد ثبت‌ها بیش از حد است.");
        JSONArray rows=new JSONArray(); for(Entry e:entries) rows.put(e.toJson());
        JSONObject envelope=new JSONObject(); envelope.put("app","HisabYar"); envelope.put("version",1);
        envelope.put("entries",rows); String json=envelope.toString();
        if(json.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>MAX_BYTES) throw new IllegalArgumentException("حجم دفتر بیش از ۸ مگابایت است؛ پشتیبان را بخش‌بندی کنید.");
        return json;
    }
}
