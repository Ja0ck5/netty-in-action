package com.ja0ck5.netty;

import com.ja0ck5.netty.decoder.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jack on 2017/9/17.
 */
public class FrameChunkDecoderTest {

	@Test
	public void testFrameChunkDecoder() {
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0; i < 9; i++) {
			buf.writeByte(i);
		}
		ByteBuf input = buf.duplicate();

		EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));
		// 写入 2 字节，并断言它们会产生一个新帧
		assertTrue(channel.writeInbound(input.readBytes(2)));
		try {
			// 写入超过解码器最大限制帧大小
			channel.writeInbound(input.readBytes(4));
			// 如果没有异常，则达到下面这个断言
			Assert.fail();
		} catch (TooLongFrameException e) {
			// expected exception
		}

		// 写入剩余的 2 字节，并断言将产生一个有效帧
		assertTrue(channel.writeInbound(input.readBytes(3)));
		// 将Channel 标记为已完成状态
		assertTrue(channel.finish());

		// Read frames 读取产生的消息并验证
		ByteBuf read = channel.readInbound();

		assertEquals(buf.readSlice(2), read);
		read.release();

		read = channel.readInbound();

		assertEquals(buf.skipBytes(4).readSlice(3), read);
		read.release();
		buf.release();

	}

}