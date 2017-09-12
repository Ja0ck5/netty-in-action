package com.ja0ck5.netty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

/**
 * 当一个应用程序必须支持多种协议时，将会有很多的 ChannelHandler
 * <p>
 * 通过 {@link io.netty.channel.ChannelInitializer#initChannel(Channel)} 方法提供了一种将多个 ChannelHandler
 * 添加到一个 ChannelPipeline 中的简便方法。 只需要向  Bootstrap 或者 ServerBootstrap 提供 你的 ChannelInitializer
 * 的实现即可。
 * 并且一旦 Channel 被注册到了它的 EventLoop 之后，就会调用你的版本的 initChannel 方法。在该方法返回之后，ChannelInitializer 的实例
 * 将会从 ChannelPipeline 中移除它自己
 * Created by Ja0ck5 on 2017/9/12.
 */
public class MultipleHandlerServerBootstrap {

    public void server() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                // 注册到 ServerChannel 的子Channel 的ChannelPipeline
                .childHandler(new ChannelInitializerImpl());// 注册 ChannelInitializerImpl 来设置 ChannelPipeline
        ChannelFuture f = serverBootstrap.bind(new InetSocketAddress(8080));
        f.sync();


    }


    /**
     * 在大部分场景下，如果你不需要使用只存在于 SocketChannel 上的方法。使用 ChannelInitializer<Channel>
     * 就够了。否则你可以使用 ChannelInitializer<SocketChanel>
     */
    final class ChannelInitializerImpl extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            // 添加多个ChannelHandler
            pipeline.addLast(new HttpClientCodec()).addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        }
    }
}
