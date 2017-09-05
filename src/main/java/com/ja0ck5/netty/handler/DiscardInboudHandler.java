package com.ja0ck5.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * 调用
 * {@link io.netty.channel.ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}
 * 或者
 * {@link io.netty.channel.ChannelOutboundHandler#write(ChannelHandlerContext, Object, ChannelPromise)}
 * 需要确保没有任何的资源泄漏，即释放资源。
 *
 * Created by Jack on 2017/9/4.
 */
public class DiscardInboudHandler extends ChannelInboundHandlerAdapter {

	/**
	 * Netty 由于消费入站数据是一项常规的任务，所以Netty 提供了一种简单实现用来自动释放消息
	 * {@link io.netty.channel.SimpleChannelInboundHandler}
	 * 
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ReferenceCountUtil.release(msg);// 释放资源
	}
}
