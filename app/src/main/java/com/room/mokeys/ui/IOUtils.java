package com.room.mokeys.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    final static int BUFFER_SIZE = 4096;
    // 将InputStream转换成byte数组
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[BUFFER_SIZE];

        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)

            outStream.write(data, 0, count);


        return outStream.toByteArray();

    }
}
