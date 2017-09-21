package com.ja0ck5.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Created by Jack on 2017/9/21.
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 该 LineBasedFrameDecoder 将提的帧转发给下个 ChannelInBoundHandler
		pipeline.addLast(new LineBasedFrameDecoder(1 << 16));
		pipeline.addLast(new FrameHandler());// 添加该 Handler 以接收 帧
	}

	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

		// 传入了单个帧的内容
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
			System.out.println(msg.toString());
		}
	}

}
