package com.ja0ck5.netty.initializer;

import com.ja0ck5.netty.handler.HttpRequestHandler;
import com.ja0ck5.netty.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Jack on 2017/9/22.
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {

	private final ChannelGroup group;

	public ChatServerInitializer(ChannelGroup group) {
		this.group = group;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		/**
		 * 将字节 解码为 HttpRequest / HttpContent / LastHttpContent. 并将 HttpRequest /
		 * HttpContent / LastHttpContent 编码为字节
		 */
		pipeline.addLast(new HttpServerCodec());
		// 写一个文件的内容
		pipeline.addLast(new ChunkedWriteHandler());
		// 下一个 ChannelHandler 只会收到完整的 Http 请求
		pipeline.addLast(new HttpObjectAggregator(1 << 16));
		// 处理 不发送到 ws URI 的请求
		pipeline.addLast(new HttpRequestHandler("ws"));
		/**
		 * 
		 * 按照 WebSocket 规范的要求，处理 WebSocket 升级握手
		 * {@link io.netty.handler.codec.http.websocketx.PingWebSocketFrame}
		 * {@link io.netty.handler.codec.http.websocketx.PongWebSocketFrame}
		 * {@link io.netty.handler.codec.http.websocketx.CloseWebSocketFrame}
		 * 
		 * 当升级完成之后，{@link WebSocketServerProtocolHandler} 会把
		 * {@link io.netty.handler.codec.http.HttpRequestDecoder} 替换成
		 * {@link io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder}
		 * 
		 * 为了最大的性能优化，它将移除任何不再被 WebSocket 连接所需要的 ChannelHandler 这也包括了
		 * {@link HttpObjectAggregator} 和 {@link HttpRequestHandler}
		 * 
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("ws"));
		/**
		 * 处理 {@link io.netty.handler.codec.http.websocketx.TextWebSocketFrame}
		 * 和 握手事件
		 */
		pipeline.addLast(new TextWebSocketFrameHandler(group));
	}
}
