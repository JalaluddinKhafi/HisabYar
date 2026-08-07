package com.hisabyar.app;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
public class EntryFilterTest {
    @Test public void combinedFiltersAndStableOrder() {
        List<Entry> all=Arrays.asList(new Entry("1","expense","5","AFN","غذا","نان","","2026-08-02"),
            new Entry("2","expense","6","AFN","غذا","نان","","2026-08-02"),
            new Entry("3","expense","7","USD","غذا","نان","","2026-08-03"));
        List<Entry> result=EntryFilter.select(all,"AFN","expense","2026-08","نان");
        assertEquals(2,result.size()); assertEquals("2",result.get(0).id); assertEquals("1",all.get(0).id);
        assertTrue(EntryFilter.select(all,null,null,null,"ناموجود").isEmpty());
    }
}
