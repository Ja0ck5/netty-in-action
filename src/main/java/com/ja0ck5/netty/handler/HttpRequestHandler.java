package com.ja0ck5.netty.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Jack on 2017/9/22.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String wsUri;

	private static final File INDEX;

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI() + "index.html";
			path = !path.contains("file:") ? path : path.substring(5);
			INDEX = new File(path);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Unable to locate index.html", e);
		}
	}

	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// 如果请求了 WebSocket 协议升级，则增加引用计数(调用 retain 方法) 并将它传递给下一个
		// ChannelInboundHandler
		String requestUri = request.uri();
		if (wsUri.equalsIgnoreCase(requestUri)) {
			ctx.fireChannelRead(request.retain());
		} else {
			// 处理 100 Continue 请求以符合 HTTP 1.1 规范
			if (HttpUtil.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}
			// 读取 html
			RandomAccessFile file = new RandomAccessFile(INDEX, "r");
			DefaultHttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; chartset=UTF-8");
			boolean keepAlive = HttpUtil.isKeepAlive(request);
			if (keepAlive) {
				// 如果请求了 keep-alive 则添加所需要的 Http 头信息
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}

			ctx.write(response);// 将 HttpResponse 写到客户端
			if (null == ctx.pipeline().get(SslHandler.class)) {
				// 检查是否有 SslHandler 存在于 pipeline 中，否则可以使用 ChunkedNioFile
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			} else {
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			// 写 LastHttpContent 并冲刷到客户端
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if (!keepAlive) {// 如果没有请求 keep-alive 则在写操作完成之后 关闭 Channel
				future.addListener(ChannelFutureListener.CLOSE);
			}

		}
	}

	private void send100Continue(ChannelHandlerContext ctx) {
		DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
