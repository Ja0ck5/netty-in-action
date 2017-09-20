package com.ja0ck5.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Created by Jack on 2017/9/20.
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

	private final boolean isClient;

	public HttpCompressionInitializer(boolean isClient) {
		this.isClient = isClient;
	}

	@Override
	protected void initChannel(Channel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		if (isClient) {
			// 如果是客户端则添加 HttpClientCodec
			pipeline.addLast("codec", new HttpClientCodec());
			// 处理来自服务器的压缩内容
		} else {
			pipeline.addLast("codec", new HttpServerCodec());
			// 服务器需要 HttpContentCompressor 来压缩数据
			pipeline.addLast("compressor", new HttpContentCompressor());
		}
	}
}
