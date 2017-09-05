package com.ja0ck5.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 因为一个 {@link ChannelHandler} 可以从属于多个 {@link io.netty.channel.ChannelPipeline}
 * 所以它也可以绑定到多个 {@link io.netty.channel.ChannelHandlerContext} 实例。这种用法的 {@link ChannelHandler}
 * 必须要使用 @ChannelHandler.Sharable 注解标注，否则试图将它添加到多个 {@link io.netty.channel.ChannelPipeline} 时将会触发异常
 * <p>
 * 为了安全地被用于多个并发的 Channel（即连接），这样的 ChannelHandler 必须是线程安全的
 * <p>
 * <b>为何要共享同一个 CChannelHandler???</b>
 * 在多个 {@link io.netty.channel.ChannelPipeline} 中安装同一个 {@link ChannelHandler} 的一个常见的问题是
 * 用于收集跨越多个 Channel 的统计信息
 * <p>
 * Created by Ja0ck5 on 2017/9/5.
 */
@ChannelHandler.Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Channel read message:" + msg);
        // 记录方法调用，并转发给下个 ChannelHandler
        ctx.fireChannelRead(msg);
    }
}
