package com.tl.netty.myrpc.netty;

import com.tl.netty.myrpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息,并且调用服务
        System.out.println("msg"+msg);
        //客户端在调用服务器的api时，我们需要定义一协议
        //比如为我们要求  每次发送消息是都必须以某个字符串开头 "HelloService#hello#你好";
        if(msg.toString().startsWith("HelloService#Hello#")) {
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
