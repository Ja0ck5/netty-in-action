package com.ja0ck5.netty.udp.monitor;

import com.ja0ck5.netty.udp.LogEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Jack on 2017/9/25.
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append(msg.getReceived()).append(" [").append(msg.getSource().toString()).append("] [")
				.append(msg.getLogfile()).append("] : ").append(msg.getMsg());
		System.out.println(builder);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		;
		ctx.close();
	}
}
