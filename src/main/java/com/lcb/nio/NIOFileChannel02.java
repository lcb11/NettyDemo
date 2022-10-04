package com.lcb.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception{

        //创建文件输入流
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream获取对应的FileChannel
        FileChannel channel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读到Buffer
        channel.read(byteBuffer);

        //将字节转换成字符串
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
