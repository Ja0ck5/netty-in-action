package com.ja0ck5.netty.exception;

import io.netty.channel.*;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 用于处理出站操作中的正常完成以及异常的选项都基于以下的通知机制
 * <p>
 * 1. 每个出站操作都将返回一个 {@link io.netty.channel.ChannelFuture} 。注册到 {@link io.netty.channel.ChannelFuture}
 * 的{@link io.netty.channel.ChannelFutureListener} 将在操作完成时被通知该操作是成功了还是出错了
 * <p>
 * 2. 几乎所有的 ChannelOutboundHandler 上的方法都会传入一个 {@link io.netty.channel.ChannelPromise} 的实例。
 * 作为 {@link io.netty.channel.ChannelFuture} 的子类， {@link io.netty.channel.ChannelPromise} 也可以被分配用于异步通知的
 * 监听器。但是，  {@link io.netty.channel.ChannelPromise} 还具有提供立即通知的可写方法
 * <p>
 * {@link ChannelPromise#setSuccess()}
 * {@link ChannelPromise#setFailure(Throwable)}
 * <p>
 * 添加 {@link io.netty.channel.ChannelFutureListener}只需要调用 {@link io.netty.channel.ChannelFuture#addListener(GenericFutureListener)}
 * 有两种方式可以做到这一点，其中最常用的是调用 出站操作(如 write()) 所返回的 {@link io.netty.channel.ChannelFuture#addListener(GenericFutureListener)}
 * <p>
 * ChannelFuture future = channel.write(message);
 * future.addListener(new ChannelFutureListener(){
 *
 * @Overrite public void operationComplete(ChannelFuture f){
 * if(!f.isSuccess()){
 * f.cause().printStackTrace();
 * f.channel().close();
 * }
 * }
 * });
 * <p>
 * <p>
 * 第二种方式：如以下代码
 * Created by Ja0ck5 on 2017/9/6.
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                channelFuture.cause().printStackTrace();
                channelFuture.channel().close();
            }
        });
    }
}
