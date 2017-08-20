package com.ja0ck5.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Ja0ck5 on 2017/8/20.
 */
public class EchoClient {

    private final String host = "127.0.0.1";
    private final int port = 9999;

    public void start() throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)//
                    // 不同的是，客户端使用主机和端口参数来连接远程地址
                    .remoteAddress(new InetSocketAddress(this.host, this.port))//
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            /**
             * 这里使用的是 {@link Bootstrap#connect()} 方法而不是 {@link Bootstrap#bind()}
             * */
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient().start();
    }


}
