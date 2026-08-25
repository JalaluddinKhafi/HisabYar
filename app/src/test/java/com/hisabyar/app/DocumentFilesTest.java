package com.hisabyar.app;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import static org.junit.Assert.*;
public class DocumentFilesTest {
    @Test public void utf8RoundTrip() throws Exception {
        ByteArrayOutputStream out=new ByteArrayOutputStream(); DocumentFiles.write(out,"پشتیبان ۱۲۳");
        assertEquals("پشتیبان ۱۲۳",DocumentFiles.read(new ByteArrayInputStream(out.toByteArray())));
    }
    @Test(expected=IOException.class) public void invalidUtf8Fails() throws Exception {
        DocumentFiles.read(new ByteArrayInputStream(new byte[]{(byte)0xC3,(byte)0x28}));
    }
    @Test(expected=IOException.class) public void oversizedFileFails() throws Exception {
        DocumentFiles.read(new ByteArrayInputStream(new byte[BackupCodec.MAX_BYTES+1]));
    }
    @Test(expected=IOException.class) public void missingStreamFails() throws Exception { DocumentFiles.read(null); }
    @Test public void closesOutputOnFailure() throws Exception {
        final boolean[] closed={false};
        OutputStream broken=new OutputStream() {
            public void write(int value) throws IOException { throw new IOException("disk full"); }
            public void close() { closed[0]=true; }
        };
        try { DocumentFiles.write(broken,"test"); fail(); } catch(IOException expected) {}
        assertTrue(closed[0]);
    }
}
