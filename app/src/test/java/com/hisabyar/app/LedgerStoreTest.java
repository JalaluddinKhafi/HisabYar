package com.hisabyar.app;
import android.content.SharedPreferences;
import java.lang.reflect.Proxy;
import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.*;
public class LedgerStoreTest {
    String stored="[]"; boolean failWrite;
    private SharedPreferences prefs() {
        return (SharedPreferences)Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{SharedPreferences.class},(proxy,method,args)->{
            if(method.getName().equals("getString")) return stored;
            if(method.getName().equals("edit")) {
                String[] pending={stored};
                return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{SharedPreferences.Editor.class},(editor,operation,values)->{
                    if(operation.getName().equals("putString")) { pending[0]=(String)values[1]; return editor; }
                    if(operation.getName().equals("commit")) { if(failWrite) return false; stored=pending[0]; return true; }
                    throw new UnsupportedOperationException(operation.getName());
                });
            }
            throw new UnsupportedOperationException(method.getName());
        });
    }
    private Entry e(String amount) { return new Entry("1","expense",amount,"AFN","غذا","","","2026-08-02"); }
    @Test public void failedWriteDoesNotMutateLedger() throws Exception {
        LedgerStore store=new LedgerStore(prefs()); store.load(); failWrite=true;
        try { store.upsert(e("5")); fail(); } catch(java.io.IOException expected) {}
        assertTrue(store.entries().isEmpty()); assertEquals("[]",stored);
    }
    @Test public void corruptLoadBlocksAllWritesAndKeepsRaw() throws Exception {
        stored="broken JSON"; LedgerStore store=new LedgerStore(prefs());
        try { store.load(); fail(); } catch(Exception expected) {}
        assertTrue(store.isReadOnly()); assertEquals(stored,store.raw());
        try { store.replace(Collections.emptyList()); fail(); } catch(java.io.IOException expected) {}
        assertEquals("broken JSON",stored);
    }
    @Test public void editDeleteAndReloadUseStableIds() throws Exception {
        LedgerStore store=new LedgerStore(prefs()); store.load(); store.upsert(e("5")); store.upsert(e("7"));
        assertEquals(1,store.entries().size()); assertEquals("7.00",store.entries().get(0).amount.toPlainString());
        LedgerStore reloaded=new LedgerStore(prefs()); reloaded.load(); assertEquals("1",reloaded.entries().get(0).id);
        Entry removed=store.remove("1"); assertTrue(store.entries().isEmpty()); store.upsert(removed); assertEquals(1,store.entries().size());
    }
    @Test public void invalidReplacementDoesNotChangeExistingLedger() throws Exception {
        LedgerStore store=new LedgerStore(prefs()); store.load(); store.upsert(e("5")); String previous=stored;
        try { store.replace(java.util.Arrays.asList(e("1"),e("2"))); fail(); } catch(IllegalArgumentException expected) {}
        assertEquals(previous,stored); assertEquals("5.00",store.entries().get(0).amount.toPlainString());
    }
    @Test public void callersCannotMutateEntries() throws Exception {
        LedgerStore store=new LedgerStore(prefs()); store.load();
        try { store.entries().add(e("1")); fail(); } catch(UnsupportedOperationException expected) {}
    }
}
