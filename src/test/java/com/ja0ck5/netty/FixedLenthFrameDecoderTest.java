package com.ja0ck5.netty;

import com.ja0ck5.netty.decoder.FixedLenthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jack on 2017/9/16.
 */
public class FixedLenthFrameDecoderTest {

	@Test
	public void testFramesDecoded() {
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0; i < 9; i++) {
			buf.writeByte(i);
		}

		ByteBuf input = buf.duplicate();
		// 创建一个 EmbeddedChannel ，并添加一个FixedLengthFrameDecoder
		EmbeddedChannel channel = new EmbeddedChannel(new FixedLenthFrameDecoder(3));
		// writeInbound 将入站消息写到 EmbeddedChannel 如果可以通过 readInbound 方法从
		// EmbeddedChannel 中读取则返回 true
		assertTrue(channel.writeInbound(input.retain()));

		// 标记为已完成状态
		assertTrue(channel.finish());

		// 从 EmbeddedChannel 中读取一个入站消息，任何返回的东西都穿越了整个 ChannelPipeline
		// 如果没有任何可供读取的，则返回 null
		// read message 读取所生成的消息，并且验证是否有 3 帧(切片),其中 每帧(切片)都为 3 字节
		ByteBuf read = channel.readInbound();
		assertEquals(buf.readSlice(3), read);
		read.release();

		read = channel.readInbound();
		assertEquals(buf.readSlice(3), read);
		read.release();

		read = channel.readInbound();
		assertEquals(buf.readSlice(3), read);
		read.release();

		assertNull(channel.readInbound());
		buf.release();
	}

	@Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();
        // 创建一个 EmbeddedChannel ，并添加一个FixedLengthFrameDecoder
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLenthFrameDecoder(3));

        assertFalse(channel.writeInbound(input.readBytes(2)));// 返回 false 因为没有一个完整的可供读取的帧
        assertTrue(channel.writeInbound(input.readBytes(7)));

        // 标记为已完成状态
        assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();

    }

}