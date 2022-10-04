package com.lcb.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 */
public class NIOFileChannel01 {
    public static void main(String[] args)throws Exception {
        String str = "hello,lcb";

        //创建一个输出流到channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        //通过filieoutputStream 获取对应的fileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);

        //将str放到ByteBuffer
        allocate.put(str.getBytes());

        //对byteBuffer反转
        allocate.flip();

        //将byte Buffer写入到fileChannel
        fileChannel.write(allocate);

        //关闭流
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
