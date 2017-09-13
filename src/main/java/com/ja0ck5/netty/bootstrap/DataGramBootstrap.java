package com.ja0ck5.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Created by Ja0ck5 on 2017/9/13.
 */
public class DataGramBootstrap {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
                        System.out.println(datagramPacket);
                    }
                });
        // 当前使用的协议时无连接的
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
        // 不再调用 connect
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Channel bound!!!");
            } else {
                System.err.println("Bind attempt failed!!!");
                channelFuture.cause().printStackTrace();
            }
        });
    }

}
