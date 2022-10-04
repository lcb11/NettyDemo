package com.lcb.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class NIOClient {
    public static void main(String[] args) throws Exception {

        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //提供服务器端的IP 和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()){
                System.out.println("因为客户端需要时间，客户端不会阻塞，可以做其他工作");
            }

        }
        //如果连接成功就发送数据
        String str = "hello,lcb";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer数据写入channel
        socketChannel.write(byteBuffer);
        System.in.read();

    }
}
