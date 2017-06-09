package com.life.common;

import java.nio.ByteBuffer;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class BytesUtils {

    /**
     * int to bytes
     *
     * @param value
     * @return
     */
    public static byte[] getBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static byte[] getBytes(String value) {
        return value.getBytes();
    }

    public static int toInt(byte[] value) throws Exception {
        if (value != null && value.length == Integer.BYTES) {
            return ByteBuffer.wrap(value).getInt();
        }
        throw new Exception("Fail convert bytes to int.");
    }

    public static String toString(byte[] value) throws Exception {
        if (value != null) {
            return new String(value);
        }

        throw new Exception("Fail convert bytes to string.");

    }

}
