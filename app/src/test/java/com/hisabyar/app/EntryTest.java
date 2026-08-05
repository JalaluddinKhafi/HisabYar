package com.hisabyar.app;
import org.junit.Test;
import org.json.JSONObject;
import static org.junit.Assert.*;
public class EntryTest {
    @Test public void legacyNumberIdAndAmountMigrate() throws Exception {
        JSONObject o=new JSONObject("{\"id\":123,\"type\":\"expense\",\"amount\":12.5,\"currency\":\"AFN\",\"category\":\"غذا\",\"date\":\"2026-08-02\"}");
        Entry e=Entry.fromJson(o);
        assertEquals("123",e.id); assertEquals("12.50",e.toJson().getString("amount"));
    }
    @Test public void uniqueIdsDoNotUseClockResolution() { assertNotEquals(Entry.newId(), Entry.newId()); }
    @Test(expected=IllegalArgumentException.class) public void invalidCurrencyFails() {
        new Entry("1","income","2","EUR","سایر","","","2026-08-02");
    }
}
