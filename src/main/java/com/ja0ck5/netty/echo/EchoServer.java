package com.ja0ck5.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Ja0ck5 on 2017/8/17.
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage " + EchoServer.class.getSimpleName() + " <port>");
        }

        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建 EventGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)// 制定使用 Nio 传输 Channel
                    .localAddress(new InetSocketAddress(port))// 使用制定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 添加一个 EchoserverHandler 到 子Channel 的 ChannelPipeline
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // EchoServerHandler 被标注为 @Shareable ,所以我们总是可以使用同样的实例
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步地绑定服务器;调用 sync() 方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            // 获取 Channel 的 CloseFuture ，并阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup ，释放所有资源
            group.shutdownGracefully();
        }
    }

}
