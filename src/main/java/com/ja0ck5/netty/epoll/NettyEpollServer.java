package com.ja0ck5.netty.epoll;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 由 JNI 驱动的 epoll() 和非阻塞 IO。这个传输支持只有在 Linux 上可用的多重特性,
 * 如 SO_REUSEPORT, 比 NIO 传输更快，而且是完全非阻塞的。
 * 如果 应用程序是运行在 Linux 上，那么请利用这个版本的传输，在高负载的情况下，性能要由于 JDK 的 NIO 实现
 * <p>
 * <p>
 * Created by Ja0ck5 on 2017/8/22.
 */
public class NettyEpollServer {

    public void server(int port) {
        final ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8));
        EpollEventLoopGroup epollGroup = new EpollEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(epollGroup)//
                    .channel(EpollServerSocketChannel.class)// 使用 NioServerSocketChannel 以允许非阻塞模式
                    .localAddress(new InetSocketAddress(port))
                    // 指定 ChannelInitializer ，对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {// 添加一个 ChannelInboundHandlerAdapter 以拦截 和 处理事件
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端，并添加 ChannelFutureListener 以便消息一被写完就关闭连接
                                    ctx.writeAndFlush(byteBuf.duplicate())
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                            ChannelFuture f = b.bind().sync();
                            f.channel().closeFuture().sync();
                        }
                    });

        } finally {
            epollGroup.shutdownGracefully();
        }

    }


}
