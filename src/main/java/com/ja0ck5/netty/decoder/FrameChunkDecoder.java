package com.ja0ck5.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 扩展 {@link ByteToMessageDecoder} 将入站字节解码为消息 Created by Jack on 2017/9/17.
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

	private final int maxFrameSize;

	public FrameChunkDecoder(int maxFrameSize) {
		// 指定将要产生的帧的最大允许大小
		this.maxFrameSize = maxFrameSize;
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
			throws Exception {
		//
		int readableBytes = byteBuf.readableBytes();
		// 如果帧太大，则丢弃它并抛出 TooLongFrameException
		if (readableBytes > maxFrameSize) {
			// discard the bytes
			byteBuf.clear();
			throw new TooLongFrameException();
		}
		ByteBuf buf = byteBuf.readBytes(readableBytes);
		list.add(buf);// 将该帧添加到消息列表
	}
}
