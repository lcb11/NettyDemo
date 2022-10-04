package com.lcb.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *Scattering 将数据写入buffer时 可以采用buffer数组，依次写入
 * gathering  采用buffer数组读入数据
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{


        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength=8;

        while (true){
            int byteRead=0;
            while (byteRead<messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead+=l;

                System.out.println("byteRead="+byteRead);
                Arrays.asList(byteBuffers).stream().map(buffer->"position="+buffer.position()+"limit+" +
                        buffer.limit()).forEach(System.out::println);
            }

            //将所有buffer进行反转
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //将数据显示会客户端
            long bytewirte=0;
            while (bytewirte<messageLength){
                long l=socketChannel.write(byteBuffers);
                bytewirte+=l;
            }

            //将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead:="+byteRead+"byteWirte:="+bytewirte+"messageLength:="+messageLength);
        }



    }
}
