package com.hisabyar.app;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
public class ExportReportTest {
    @Test public void categoryReportSeparatesCurrencyAndMonth() {
        String report=CategoryReport.expenses(Arrays.asList(
            LedgerTest.e("1","expense","2","AFN","2026-08-02"),
            LedgerTest.e("2","expense","3","USD","2026-08-02"),
            LedgerTest.e("3","expense","99","AFN","2026-09-02")),"2026-08");
        assertTrue(report.contains("2.00")); assertTrue(report.contains("3.00")); assertFalse(report.contains("99.00"));
    }
    @Test public void csvQuotesNewlinesAndNeutralizesFormulas() {
        Entry e=new Entry("1","income","1","USD","سایر"," =SUM(A1)","a,\"b\"\nc","2026-08-02");
        String csv=CsvExport.encode(Arrays.asList(e));
        assertTrue(csv.startsWith("\uFEFFid,")); assertTrue(csv.contains("'=SUM(A1)"));
        assertTrue(csv.contains("a,\"\"b\"\"\nc")); assertTrue(csv.endsWith("\r\n"));
    }
}
