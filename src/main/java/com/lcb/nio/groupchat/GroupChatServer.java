package com.lcb.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 *
 */
public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenchannel;
    private static final int PROT=6667;

    //构造器
    public GroupChatServer(){
        try{
            //得到选择器
            selector=Selector.open();
            //得到ServerSocketChannel
            listenchannel=ServerSocketChannel.open();
            //绑定端口
            listenchannel.socket().bind(new InetSocketAddress(PROT));
            //设置非阻塞模式
            listenchannel.configureBlocking(false);
            //将listenchannel注册到selector
            listenchannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try{
            //循环处理
            while (true){

                int count=selector.select(2000);
                if (count > 0) {//有事件要处理
                    //遍历得到的SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = iterator.next();

                        //监听到accept
                        if(key.isAcceptable()){
                            SocketChannel sc = listenchannel.accept();
                            sc.configureBlocking(false);
                            //将该sc注册到selector
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getLocalAddress()+"上线");
                        }

                        //通道发送read事件，即通道可读状态
                        if(key.isReadable()){
                            //处理读（专门方法
                            readData(key);
                        }

                        //把当前SelectionKey删除，防止重复操作
                        iterator.remove();
                    }

                }else {
                    System.out.println("等待中....");
                }

            }
    }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        }

        //读取客户端消息
    private void readData(SelectionKey key){

        //定义一个Socket Channel
        SocketChannel channel=null;

        try {
            //得到channel
            channel=(SocketChannel) key.channel();
            //创建缓存
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);
            //根据count值做处理
            if(count>0){
                //把缓存区的数据转成字符串
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("from 客户端："+msg);

                //向其他客户端转发消息，专门写一个方法
                sendInfoToOtherClients(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //转发消息给其他客户（通道）
    private void sendInfoToOtherClients(String msg,SocketChannel self)throws IOException{
        System.out.println("服务器转发消息中");

        //遍历 所有注册到selector上的Socket Channel，并排除self
        for (SelectionKey key : selector.keys()) {
            //通过key取出SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if(targetChannel instanceof SocketChannel&&targetChannel!=self){
                //转型
               SocketChannel dest= (SocketChannel)targetChannel;
               //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写入到通道
                dest.write(buffer);
            }

        }

        }



    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
