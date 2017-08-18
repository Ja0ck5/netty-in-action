package com.ja0ck5.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * <p>{@link io.netty.channel.ChannelHandler.Sharable }标示一个 ChannelHandler 可以被多个 Channel 安全的共享
 * <p>
 * <p>因为 Echo 服务器会响应传入的消息，所以需要实现 {@link io.netty.channel.ChannelInboundHandler} 接口,
 * 用来定义 响应入站事件的方法。因为这个 demo 只需要用到少量这些方法，所以集成 {@link ChannelInboundHandlerAdapter}
 * 也就足够了
 * <p>
 * <p>可能由入站数据或者相关的状态更改而触发的事件包括
 * <ul>
 * <p>
 * <li>连接已激活或者失活</li>
 * <li>数据读取</li>
 * <li>用户事件 ???</li>
 * <li>错误事件</li>
 * </ul>
 * </p>
 * <p>
 * <p>出战事件是未来将会触发的某个动作的操作结果
 * <li>打开或者关闭 到远程节点的连接</li>
 * <li>将数据写到或者冲刷到套接字</li>
 * <p>
 * <p>
 * <p>Created by Ja0ck5 on 2017/8/16.
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入消息都要调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        // 将接收到的消息写给发送者，但不冲刷出战消息
        ctx.write(in);
    }

    /**
     * <p>
     * 通知 {@link io.netty.channel.ChannelInboundHandler} 最后一次对 {@link #channelRead(ChannelHandlerContext, Object)}
     * 的调用是当前批量读取中的最后一条消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到远程节点，并且关闭该 channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 如果不捕获异常，会发生什么？
     * 每个 Channel 都拥有一个与之相关联的 {@link io.netty.channel.ChannelPipeline},其持有一个 {@link ChannelHandler} 的
     * 实例链。在默认的情况下，{@link ChannelHandler} 会把对它的调用转发给链中的下一个 {@link ChannelHandler}。
     * 因此，如果 {@link #exceptionCaught(ChannelHandlerContext, Throwable)} 没有被该链中的某处实现，那么所接收的异常
     * 将会被传递到 {@link io.netty.channel.ChannelPipeline} 的尾端并记录。
     * 为此，应该提供至少有一个实现了 {@link #exceptionCaught(ChannelHandlerContext, Throwable)} 方法的 {@link ChannelHandler}
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
