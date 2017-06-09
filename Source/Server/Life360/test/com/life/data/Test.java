package com.life.data;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Test {

    public static void main(String[] args) {
//
////        ByteBuffer allocate = ByteBuffer.allocate(16);
////        allocate.putInt(123);
////        allocate.putInt(124);
////        allocate.putInt(125);
////        allocate.putInt(126);
//
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        
//        out.writeInt(123);
//        out.writeInt(124);
//        out.writeInt(125);
//        out.writeInt(126);
//        
//     
//        ByteBuffer.
//
//        List<Byte> list = Arrays.asList(ArrayUtils.toObject(out.toByteArray()));
//        System.out.println(list);
//
//        byte[] array = out.toByteArray();
//        
//        
//        //ByteBuffer bb = ByteBuffer.wrap(array, 0, 4);
////        allocate.position(0);
////        System.out.println(allocate.getInt());
//
//        System.out.println(7 / 4);

       String i = "1";
       
        byte[] serialize = SerializationUtils.serialize(i);
        System.out.println("");

    }
}
