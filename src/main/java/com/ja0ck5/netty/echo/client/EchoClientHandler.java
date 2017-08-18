package com.ja0ck5.netty.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 为什么客户端使用的是 {@link SimpleChannelInboundHandler} ,而不是在 {@link com.ja0ck5.netty.echo.server.EchoServerHandler}
 * 中所使用的 {@link io.netty.channel.ChannelInboundHandlerAdapter} ?
 * 两个因素：
 * <li>业务逻辑如何处理消息</li>
 * <li>Netty 如何管理资源</li>
 * <p>
 * <p>
 * <p>
 * 在客户端，当 {@link #channelRead0(ChannelHandlerContext, ByteBuf)} 方法完成时，你已经有了 传入消息 ，并且已经处理完它了。
 * 当该方法返回时，{@link SimpleChannelInboundHandler} 负责释放指向保存该消息的 ByteBuf 的内存引用。
 * </p>
 * <p>
 * <p>
 * <p>
 * 在 {@link com.ja0ck5.netty.echo.server.EchoServerHandler} 中，你仍然需要将传入消息回送给发送者,
 * 而{@link ChannelHandlerContext#write(Object)} 操作是异步地，直到 {@link #channelRead(ChannelHandlerContext, Object)}
 * 方法返回后可能仍然没有完成。为此，{@link com.ja0ck5.netty.echo.server.EchoServerHandler} 扩展了 {@link io.netty.channel.ChannelInboundHandlerAdapter}
 * 其在这个时间点 是不会释放消息的。
 * <p>
 * 消息在 {@link #channelReadComplete(ChannelHandlerContext)} 中 当{@link ChannelHandlerContext#writeAndFlush(Object)} 方法被调用时释放
 * <p>
 * </p>
 * Created by Ja0ck5 on 2017/8/18.
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 在到服务器的连接已经建立之后被调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知的 channel 激活时，发送此消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    /**
     * 当从服务器接收到一条消息的时候被调用
     * 每当接收数据的时候，都会调用此方法。需要注意的是，由于服务器发送的消息可能会被分块接收。
     * 入股哦服务器发送了 5 字节，那么不能保证这 5 字节的数据会被一次性接收。
     * 即使是对于这么少量的数据,当前方法也可能被调用两次，假设第一次使用一个持有 3 字节的 {@link ByteBuf}
     * 第二次使用一个持有两个字节的 {@link ByteBuf}. 作为一个面向流的协议，TCP 保证了字节数组将会按照服务器发送它们的顺序被接收
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        // 记录已接收，消息的转储
        System.out.println("Client received!!! " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
