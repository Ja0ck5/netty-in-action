package com.ja0ck5.netty.initializer;

import io.netty.channel.*;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

/**
 * Created by Jack on 2017/9/21.
 */
public class MarshallingInitializer extends ChannelInitializer<Channel> {

	private final MarshallerProvider marshallerProvider;

	private final UnmarshallerProvider unmarshallerProvider;

	public MarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
		this.marshallerProvider = marshallerProvider;
		this.unmarshallerProvider = unmarshallerProvider;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 添加 MarshallingDecoder 以将 ByteBuf 转换为 POJO
		pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
		// 以将 POJO 转换为 ByteBuf
		pipeline.addLast(new MarshallingEncoder(marshallerProvider));
		// 以处理普通的实现了 Serializable 接口的 POJO
		pipeline.addLast(new ObjectHandler());
	}

	public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
			System.out.println(msg);
		}
	}
}
