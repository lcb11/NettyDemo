package com.lcb.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 */
public class NettyServer {
    public static void main(String[] args)throws Exception {

        //创建Boss Group 和WorkGroup
        //bossGroup只处理连接请求
        //workGroup真正和客户端业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来设置
            bootstrap.group(bossGroup, workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动的连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //创建一个通道初始化对象（匿名对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandker());
                        }
                    });//给我们的workGroup的EventLoop对应的管道设置处理器

            System.out.println("....服务器is ready....");

            //绑定端口，并同步处理  生成一个channelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听事件，监听关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("监听端口成功...");
                    }else {
                        System.out.println("监听端口失败...");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
