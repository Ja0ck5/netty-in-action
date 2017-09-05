package com.ja0ck5.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Jack on 2017/9/4.
 */
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {

	/**
	 * 重要的是，不仅要释放资源，还要通知 {@link ChannelPromise} 。否则可能会出现
	 * {@link io.netty.channel.ChannelFutureListener} 收不到某个消息已经被处理了的通知
	 * 
	 * 
	 * 总之，如果一个消息被消费或者丢弃了，并且没有传递给 {@link io.netty.channel.ChannelPipeline} 中的下一个
	 * {@link io.netty.channel.ChannelOutboundHandler},那么用户就有责任调用
	 * {@link ReferenceCountUtil#release(Object)} 如果消息到达了实际的传输层，那么当它被写入时或者
	 * Channel 关闭时，都将被自动释放
	 *
	 * @param ctx
	 * @param msg
	 * @param promise
	 * @throws Exception
	 */
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		ReferenceCountUtil.release(msg);// 释放资源
		promise.setSuccess();// 通知 ChannelPromise 数据已经被处理了
	}
}
