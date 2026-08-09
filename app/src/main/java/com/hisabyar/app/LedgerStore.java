package com.hisabyar.app;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class LedgerStore {
    private final SharedPreferences prefs;
    private List<Entry> entries=new ArrayList<>();
    public LedgerStore(SharedPreferences prefs) { this.prefs=prefs; }
    public List<Entry> entries() { return Collections.unmodifiableList(entries); }
    private boolean readOnly;
    public boolean isReadOnly() { return readOnly; }
    public String raw() { return prefs.getString("entries","[]"); }
    public void load() throws Exception {
        try { entries=BackupCodec.decode(raw()); readOnly=false; }
        catch(Exception error) { readOnly=true; throw error; }
    }
    public void upsert(Entry entry) throws Exception {
        List<Entry> candidate=new ArrayList<>(entries); boolean found=false;
        for(int i=0;i<candidate.size();i++) if(candidate.get(i).id.equals(entry.id)) {
            candidate.set(i,entry); found=true; break;
        }
        if(!found) candidate.add(entry);
        replace(candidate);
    }
    public Entry remove(String id) throws Exception {
        List<Entry> candidate=new ArrayList<>(entries);
        for(int i=0;i<candidate.size();i++) if(candidate.get(i).id.equals(id)) {
            Entry removed=candidate.remove(i); replace(candidate); return removed;
        }
        throw new IllegalArgumentException("ثبت پیدا نشد.");
    }
    public void replace(List<Entry> candidate) throws Exception {
        if(readOnly) throw new java.io.IOException("اطلاعات موجود خوانده نشد؛ ابتدا نسخهٔ خام را ذخیره و مشکل را بررسی کنید.");
        String json=BackupCodec.encode(candidate);
        List<Entry> checked=BackupCodec.decode(json);
        if(!prefs.edit().putString("entries",json).commit()) throw new java.io.IOException("ذخیره‌سازی انجام نشد. فضای آزاد دستگاه را بررسی کنید.");
        entries=checked;
    }
}
