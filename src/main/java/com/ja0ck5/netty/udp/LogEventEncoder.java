package com.ja0ck5.netty.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by Jack on 2017/9/25.
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

	private final InetSocketAddress remoteAddress;

	/**
	 * LogEnventEncoder 创建了即将被发送到指定的 {@link InetSocketAddress} 的
	 * {@link java.net.DatagramPacket} 消息
	 * 
	 * @param remoteAddress
	 */
	public LogEventEncoder(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LogEvent msg, List<Object> out) throws Exception {

	}
}
