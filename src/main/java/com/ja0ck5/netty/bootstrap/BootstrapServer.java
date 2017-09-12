package com.ja0ck5.netty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Ja0ck5 on 2017/9/12.
 */
public class BootstrapServer {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // create serverBootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置 EventLoopGroup 提供了用于处理 Channel 时间的 EventLoop
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)// 指定要使用的 Channel 实现
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {// 设置用于处理已被接受的 子 Channel 的 IO 以及数据的 ChannelInboundHandler
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data!!!");
                    }
                });

        // 通过配置好的 ServerBootstrap 的实例绑定该 Channel
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8080));
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Server bound");
            } else {
                System.err.println("Bound attempt failed!!!");
                channelFuture.cause().printStackTrace();
            }
        });
    }

}
