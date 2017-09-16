package com.ja0ck5.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by Jack on 2017/9/16.
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
		ByteBuf buf = (ByteBuf) o;
		while (buf.readableBytes() >= 4) { // 检查是否有足够的字节用来编码
			int value = Math.abs(buf.readInt());
			list.add(value);// 将整数添加到编码消息 List
		}
	}
}
