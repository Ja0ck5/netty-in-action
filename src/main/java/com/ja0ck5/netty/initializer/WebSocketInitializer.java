package com.ja0ck5.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 
 * 要想保护 WebSocket 安全性，只需要将 {@link io.netty.handler.ssl.SslHandler} 作为第一个
 * ChannelHandler 添加到 {@link io.netty.channel.ChannelPipeline}
 * 
 * 
 * Created by Jack on 2017/9/20.
 */
public class WebSocketInitializer extends ChannelInitializer<Channel> {
	@Override
	protected void initChannel(Channel channel) throws Exception {
		channel.pipeline().addLast(new HttpServerCodec(), //
				new HttpObjectAggregator(65536), // 为握手提供聚合的 HttpRequest
				new WebSocketServerProtocolHandler("/websocket"), // 如果被请求的端点是
																	// "/websocket"
																	// 则处理该升级握手
				new TextFrameHandler(), new BinaryFrameHandler(), new ContinuationFrameHandler());

	}

	public static class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame)
				throws Exception {
			// handle text frame
		}
	}

	public static class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext,
				BinaryWebSocketFrame binaryWebSocketFrame) throws Exception {
			// handle binary frame
		}
	}

	public static class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext,
				ContinuationWebSocketFrame continuationWebSocketFrame) throws Exception {
			// handle continuation frame
		}
	}
}
