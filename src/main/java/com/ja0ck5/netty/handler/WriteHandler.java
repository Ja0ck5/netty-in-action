package com.ja0ck5.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 你可以通过调用 {@link ChannelHandlerContext#pipeline()} 方法来获得封闭的 {@link io.netty.channel.ChannelPipeline} 引用
 * <p>
 * 这使得可以再运行时操作 {@link io.netty.channel.ChannelPipeline} 的 {@link io.netty.channel.ChannelHandler}
 * <p>
 * 可以利用这一点来实现复杂的设计
 * <p>
 * 1. 可以通过将 {@link io.netty.channel.ChannelHandler} 添加到 {@link io.netty.channel.ChannelPipeline} 中来实现动态的协议切换
 * <p>
 * 2. 缓存{@link ChannelHandlerContext} 引用以供稍后使用，这可能发生在任何的 ChannelHandler 方法之外，设甚至来自不同的线程
 * Created by Ja0ck5 on 2017/9/5.
 */
public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx; // 存储到 ChannelHandlerContext 的引用以供稍后使用
    }

    /**
     * 使用之前存储的到 ChannelHandlerContext 的引用来发消息
     *
     * @param msg
     */
    public void send(String msg) {
        this.ctx.writeAndFlush(msg);
    }
}
