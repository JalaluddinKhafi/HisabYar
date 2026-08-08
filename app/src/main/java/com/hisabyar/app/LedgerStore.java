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
    public void load() throws Exception { entries=BackupCodec.decode(prefs.getString("entries","[]")); }
    public void replace(List<Entry> candidate) throws Exception {
        String json=BackupCodec.encode(candidate);
        List<Entry> checked=BackupCodec.decode(json);
        if(!prefs.edit().putString("entries",json).commit()) throw new java.io.IOException("ذخیره‌سازی انجام نشد. فضای آزاد دستگاه را بررسی کنید.");
        entries=checked;
    }
}
