package com.hisabyar.app;
import org.junit.Test;
import static org.junit.Assert.*;
public class DatesTest {
    @Test public void leapDayIsValid() { assertEquals("2024-02-29",Dates.validate("2024-02-29")); }
    @Test public void invalidDatesAreRejected() {
        for (String date:new String[]{"2026-02-29","2026-13-01","2026-8-02","2026-08-02x","0000-01-01"}) {
            try { Dates.validate(date); fail(date); } catch (IllegalArgumentException expected) {}
        }
    }
}
