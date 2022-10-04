package com.lcb.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 *自定义一个handler 需要netty规定好的某HandlerAdapter
 * 这时自定义一个handler 才能称为一个handler
 */
public class NettyServerHandker extends ChannelInboundHandlerAdapter {


    //实际读取客户端发送的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx="+ctx);

        //将msg转成ByteBuf(由netty提供)ByteBuffer由NIo提供
       ByteBuf buf= (ByteBuf)msg;
        System.out.println("客户端发送的消息是："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存 并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端-",CharsetUtil.UTF_8));
    }


    //处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
