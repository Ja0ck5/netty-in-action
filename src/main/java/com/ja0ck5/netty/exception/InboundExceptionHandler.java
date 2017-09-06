package com.ja0ck5.netty.exception;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 因为异常将会继续按照入站方向流动，就像所有入站事件一样，所以以下的实现通常位于 {@link io.netty.channel.ChannelPipeline}
 * 的末端。这确保所有的异常都总是会被处理。
 * 如果不实现任何处理入站异常的逻辑，或者没有消费该异常，那么 Netty 将会记录该异常没有被处理的事实，即 Netty 将会通过 Warning 级别的日志
 * 记录该异常到达了 {@link io.netty.channel.ChannelPipeline}的末端，但没有被处理，并尝试释放该异常。
 * Created by Ja0ck5 on 2017/9/6.
 */
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
