package com.tl.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 1.我们自定义一个Handler 需要继续netty规定好的某个HandlerAdapter(规范)
 * 2.这时我们定义一个Handler，才能成为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据实际（这里我们可以读取客户端发送的消息）
    /**
     *读取实际数据
     * @param ctx 上下文对象，含有管道pipeline，通道channel，地址
     * @param msg 客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //比如这里我们有一个非常耗费时间的业务 - >异步执行->提交该channel对应的
        //NIOEventLoop的taskQueue中，
        //解决方案1  用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~:(>^w^<)喵2",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~:(>^w^<)喵3",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }

        });
        //解决方案2
        //用户自定义定时任务 -> 该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~:(>^w^<)喵4",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);

//
//        Thread.sleep(10*1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~:(>^w^<)喵2",CharsetUtil.UTF_8));
        System.out.println("go on ...");
//        System.out.println("服务器读取线程" + Thread.currentThread().getName());
//        System.out.println("server ctx =" + ctx);
//        System.out.println("看看channel 和 pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); //本质是个双向链表，出栈入栈问题
//        //将msg转成一个ByreBuf
//        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息:"+buf.toString(CharsetUtil.UTF_8));
//
//        System.out.println("客户端地址: " + ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush是write + flush
        //将数据写入到缓存，并且刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~:(>^w^<)喵1",CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
