package com.ja0ck5.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jack on 2017/9/20.
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {
	@Override
	protected void initChannel(Channel channel) throws Exception {
		// IdleStateHandler 将在被触发时发送一个 IdleStateEvent 事件
		channel.pipeline().addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))//
				.addLast(new HeartBeatHandler());// 将 HeartBeatHandler 添加到
													// pipeline 中
	}

	public static final class HeartBeatHandler extends ChannelInboundHandlerAdapter {

		/**
		 * 发送到远程节点的心跳消息
		 */
		private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
				.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));

		// 实现 userEventTriggered 以发送心跳消息
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent) {// 发送心跳消息，并在发送失败时关闭该连接
				ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			} else {// 不是 IdleStateEvent 就传递到下一个 ChannelInboundHandler
				super.userEventTriggered(ctx, evt);
			}
		}
	}
}
