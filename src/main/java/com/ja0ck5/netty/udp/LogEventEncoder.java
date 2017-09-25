package com.ja0ck5.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

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
		byte[] file = msg.getLogfile().getBytes(CharsetUtil.UTF_8);
		byte[] message = msg.getMsg().getBytes(CharsetUtil.UTF_8);
		ByteBuf buf = ctx.alloc().buffer(file.length + message.length + 1);
		buf.writeBytes(file);// 将文件名写入 ByteBuf中
		buf.writeByte(LogEvent.SEPARATOR);
		buf.writeBytes(message);
		// 将一个拥有数据和目的地地址的 新 DatagramPacket 添加到出站的消息列表中
		out.add(new DatagramPacket(buf,remoteAddress));
	}
}
