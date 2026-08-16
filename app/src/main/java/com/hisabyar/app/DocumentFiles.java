package com.hisabyar.app;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
public final class DocumentFiles {
    private DocumentFiles() {}
    public static String read(InputStream stream) throws IOException {
        if(stream==null) throw new IOException("فایل باز نشد.");
        try(InputStream input=stream; ByteArrayOutputStream buffer=new ByteArrayOutputStream()) {
            byte[] bytes=new byte[8192]; int count;
            while((count=input.read(bytes))!=-1) {
                if(buffer.size()+count>BackupCodec.MAX_BYTES) throw new IOException("فایل بزرگ‌تر از ۸ مگابایت است.");
                buffer.write(bytes,0,count);
            }
            return new String(buffer.toByteArray(),StandardCharsets.UTF_8);
        }
    }
    public static void write(OutputStream stream,String text) throws IOException {
        if(stream==null) throw new IOException("فایل برای نوشتن باز نشد.");
        try(OutputStream output=stream) { output.write(text.getBytes(StandardCharsets.UTF_8)); output.flush(); }
    }
}
