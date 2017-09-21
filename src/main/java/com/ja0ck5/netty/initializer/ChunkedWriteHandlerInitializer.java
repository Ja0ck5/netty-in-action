package com.ja0ck5.netty.initializer;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * {@link io.netty.handler.stream.ChunkedInput} 要使用你自己的 ChunkedInput
 * 实现，需要在{@link ChannelPipeline} 中安装 {@link ChunkedWriteHandler}
 * 
 * Created by Jack on 2017/9/21.
 */
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

	private final File file;

	private final SslContext sslContext;

	public ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
		this.file = file;
		this.sslContext = sslContext;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 将 SslHandler 添加到 ChannelPipeline 中
		pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc())));
		// 已处理作为 ChunkedInput 传入的数据
		pipeline.addLast(new ChunkedWriteHandler());
		// 一旦建立连接，就开始写文件数据
		pipeline.addLast(new WriteStreamHandler());
	}

	public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

		/**
		 * 当建立连接时，channelActive 方法开始使用
		 * {@link io.netty.handler.stream.ChunkedInput} 开始写入文件数据
		 * 
		 * @param ctx
		 * @throws Exception
		 */
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
		}
	}
}
