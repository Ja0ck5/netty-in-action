package com.ja0ck5.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Jack on 2017/9/16.
 */
public class FixedLenthFrameDecoder extends ByteToMessageDecoder {

	private final int frameLen;

	public FixedLenthFrameDecoder(int frameLen) {
		if (frameLen <= 0) {
			throw new IllegalArgumentException("frameLen must be a positive integer:" + frameLen);
		}
		this.frameLen = frameLen;

	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
			throws Exception {

		while (byteBuf.readableBytes() >= frameLen) {
			{
				ByteBuf buf = byteBuf.readBytes(frameLen);// 读取固定大小字节的 ByteBuf
				list.add(buf);
			}
		}

	}
}
