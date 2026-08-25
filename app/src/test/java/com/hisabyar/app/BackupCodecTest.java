package com.hisabyar.app;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.json.JSONObject;
import static org.junit.Assert.*;
public class BackupCodecTest {
    final Entry e=new Entry("1","income","1.20","USD","معاش","شرکت","یادداشت","2026-08-02");
    @Test public void backupRoundTripPreservesDecimalAndText() throws Exception {
        List<Entry> decoded=BackupCodec.decode(BackupCodec.encode(Arrays.asList(e)));
        assertEquals(e.amount,decoded.get(0).amount); assertEquals(e.note,decoded.get(0).note);
    }
    @Test public void byteOrderMarkIsAccepted() throws Exception {
        assertEquals(1,BackupCodec.decode("\uFEFF"+BackupCodec.encode(Arrays.asList(e))).size());
    }
    @Test public void legacyArrayLoads() throws Exception {
        assertEquals("1",BackupCodec.decode("["+e.toJson()+"]").get(0).id);
    }
    @Test(expected=IllegalArgumentException.class) public void duplicateIdsFail() throws Exception {
        BackupCodec.decode("["+e.toJson()+","+e.toJson()+"]");
    }
    @Test(expected=IllegalArgumentException.class) public void unknownVersionFails() throws Exception {
        BackupCodec.decode(new JSONObject(BackupCodec.encode(Arrays.asList(e))).put("version",2).toString());
    }
    @Test(expected=IllegalArgumentException.class) public void fractionalVersionFails() throws Exception {
        BackupCodec.decode(new JSONObject(BackupCodec.encode(Arrays.asList(e))).put("version",1.5).toString());
    }
    @Test public void malformedRowIsNotSilentlySkipped() throws Exception {
        try { BackupCodec.decode("["+e.toJson()+",{}]"); fail(); } catch(org.json.JSONException expected) {}
    }
}
