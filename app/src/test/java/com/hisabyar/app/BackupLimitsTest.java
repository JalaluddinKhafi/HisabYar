package com.hisabyar.app;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
public class BackupLimitsTest {
    private JSONObject row() throws Exception {
        return new Entry("1","expense","1","AFN","سایر","","","2026-08-02").toJson();
    }
    @Test public void extremeNumericExponentsFailWithoutExpandingStrings() throws Exception {
        for(String exponent:new String[]{"1e2147483647","1e-2147483647","1e1000000","1e-1000000"}) {
            JSONObject json=row(); json.put("amount",new java.math.BigDecimal(exponent));
            try { Entry.fromJson(json); fail(exponent); } catch(IllegalArgumentException expected) {}
        }
    }
    @Test public void tooManyEntriesFailBeforeDecodingRows() throws Exception {
        JSONArray rows=new JSONArray(); for(int i=0;i<=BackupCodec.MAX_ENTRIES;i++) rows.put(new JSONObject());
        try { BackupCodec.decode(rows.toString()); fail(); } catch(IllegalArgumentException expected) {}
    }
    @Test public void encodedBackupsFitTheImportByteLimit() throws Exception {
        char[] chars=new char[1000]; Arrays.fill(chars,'غ'); String note=new String(chars);
        java.util.ArrayList<Entry> rows=new java.util.ArrayList<>();
        for(int i=0;i<4200;i++) rows.add(new Entry(Integer.toString(i),"income","1","AFN","سایر","",note,"2026-08-02"));
        try { BackupCodec.encode(rows); fail(); } catch(IllegalArgumentException expected) {}
    }
    @Test public void textFieldLimitsAreEnforcedBeforeStorage() throws Exception {
        char[] chars=new char[1001]; Arrays.fill(chars,'x');
        JSONObject json=row().put("note",new String(chars));
        try { Entry.fromJson(json); fail(); } catch(IllegalArgumentException expected) {}
    }
}
