package com.hisabyar.app;
public final class CsvExport {
    private CsvExport() {}
    private static String cell(String text) {
        // Guard spreadsheet formula execution for user-authored cells, including leading whitespace.
        String trimmed=text.trim();
        if(!trimmed.isEmpty() && "=+-@".indexOf(trimmed.charAt(0))>=0) text="'"+text;
        return "\""+text.replace("\"","\"\"")+"\"";
    }
    public static String encode(java.util.List<Entry> entries) {
        StringBuilder out=new StringBuilder("\uFEFFid,date,type,amount,currency,category,source,note\r\n");
        for(Entry e:entries) {
            String[] row={e.id,e.date,e.type,e.amount.toPlainString(),e.currency,e.category,e.source,e.note};
            for(int i=0;i<row.length;i++) { if(i>0) out.append(','); out.append(cell(row[i])); }
            out.append("\r\n");
        }
        return out.toString();
    }
}
