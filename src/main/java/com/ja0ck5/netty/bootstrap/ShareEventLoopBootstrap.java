package com.ja0ck5.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 假设你的服务器正在处理一个客户端的请求。这个请求需要服务器充当第三方系统的客户端。当一个应用程序
 * （如代理服务器）必须要和组织现有的系统集成时 （如Web服务或者数据库）就可能发生这种情况。 这种情况下
 * 需要从已被接受的子 Channel 中引导一个客户端 Channel
 * <p>
 * 解决方案：实现 EventLoop 在 已被接受的连接创建的 子Channel 和 由 connect 方法锁创建的 Channel说 之间共享
 * <p>
 * 尽可能地重用 EventLoop ，以减少线程创建所带来的开销
 * Created by Ja0ck5 on 2017/9/12.
 */
public class ShareEventLoopBootstrap {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置 EventLoopGroup 将其提供用以 Channel 时间的 EventLoop
        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)// 指定 Channel 要用的实现
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    // bootstrap connect future
                    ChannelFuture connectFuture;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        // 创建 Bootstrap 类的实例以连接到远程主机
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.channel(NioSocketChannel.class)//指定 Channel 要用的实现
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {// 为入站 IO 设置 ChannelInboundHandler
                                    // in bootstrap
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                        System.out.println("Received data!!!");
                                    }
                                });
                        // ======= 使用与非配给已被接受的 子 Channel 相同的 EventLoop =======//
                        bootstrap.group(ctx.channel().eventLoop());
                        connectFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
                    }

                    // in server bootstrap
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        if (connectFuture.isDone()) {
                            // 当连接完成时，执行一些数据操作（如代理）
                            System.out.println(byteBuf);
                        }
                    }
                });
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8080));// binding该 ServerSocketChannel
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Server bound!!!");
            } else {
                System.err.println("Bind attempt failed!!!");
                channelFuture.cause().printStackTrace();
            }
        });
    }

}
