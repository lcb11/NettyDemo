package com.lcb.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 *
 */
public class NIOServer {
    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个selecor对象
        Selector selector = Selector.open();

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector  关心时间OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户连接
        while (true){
            if(selector.select(1000)==0){
                System.out.println("等待了1秒，无连接");
                continue;
            }

            //如果返回的不是0 就获取相关的selectionKeys 即关注时间集合的key
            Set<SelectionKey> selectionKeys = selector.selectedKeys();


            //使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到selectionKeys
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){//有新的客户端来连接
                    //给该客户端生成新的SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将当前socketChannel注册到selector 关注事件为OP_READ 同时给socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if(key.isReadable()){
                    //通过key反向获取channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer =(ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("from客户端"+new String(buffer.array()));
                }
                //手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
