package com.ja0ck5.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by Jack on 2017/9/21.
 */
public class LengthBasedInitializer extends ChannelInitializer<Channel> {
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new LengthFieldBasedFrameDecoder(1 << 16, 0, 1 << 3));
		pipeline.addLast(new FrameHandler());// 添加 handler 以处理帧
	}

	public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
			System.out.println("msg:" + msg);// 处理帧数据
		}
	}
}
