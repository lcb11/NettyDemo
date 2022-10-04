package com.lcb.nio;

import java.nio.ByteBuffer;

/**
 *
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {

        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        //类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('流');

        //反转
        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());

    }
}
