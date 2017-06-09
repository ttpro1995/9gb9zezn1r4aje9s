package com.life.common;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GzipHelper {

    private static final Logger logger = LogManager.getLogger(GzipHelper.class);

    public static byte[] gzip(byte[] comDataRaw) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            GZIPOutputStream gos = new GZIPOutputStream(bos);
            gos.write(comDataRaw);
            gos.finish();

            return bos.toByteArray();
        } catch (Exception e) {
            logger.error("", e);
        }

        return new byte[0];
    }

    public static String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }

    public static byte[] ungzip(byte[] gzipData) {
        GZIPInputStream gzis = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream(1024);
            gzis = new GZIPInputStream(new ByteArrayInputStream(gzipData));

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = gzis.read(buffer)) > 0) {
                baos.write(buffer, 0, count);
            }
            baos.flush();

            return baos.toByteArray();

        } catch (IOException e) {
            logger.error("", e);
        } finally {
            try {
                if (gzis != null) {
                    gzis.close();
                }
            } catch (Exception e) {
            }

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

}
