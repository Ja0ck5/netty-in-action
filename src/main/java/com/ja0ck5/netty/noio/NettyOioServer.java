package com.ja0ck5.netty.noio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created by Ja0ck5 on 2017/8/22.
 */
public class NettyOioServer {

    public void server(int port) {
        final ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8));
        OioEventLoopGroup oioGroup = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(oioGroup)//
                    .channel(OioServerSocketChannel.class)// 使用OioEventLoopGroup 以允许阻塞模式
                    .localAddress(new InetSocketAddress(port))
                    // 指定 ChannelInitializer ，对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {// 添加一个 ChannelInboundHandlerAdapter 以拦截 和 处理事件
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端，并添加 ChannelFutureListener 以便消息一被写完就关闭连接
                                    ctx.writeAndFlush(byteBuf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                            ChannelFuture f = b.bind().sync();
                            f.channel().closeFuture().sync();
                        }
                    });

        } finally {
            oioGroup.shutdownGracefully();
        }

    }

}
