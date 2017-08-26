package com.ja0ck5.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by Jack on 2017/8/26.
 */
public class ByteBufTest {

	/**
	 * 使用一些 需要 “一个索引值参数的方法” 之一来访问数据，既不会改变 readerIndex,也不会改变 writerIndex 。
	 * 如果有需要也可以通过调用 {@link ByteBuf#readerIndex(int)} or
	 * {@link ByteBuf#writerIndex(int)} 来手动移动
     *
     * <p>
     *     {@link ByteBuf} 具有读索引和写索引，但是 jdk {@link java.nio.ByteBuffer} 只有一个索引，也就是为什么需要调用{@link ByteBuffer#flip()}
     *     方法来在 读模式 和 写模式在之间进行切换的原因。
     * </p>
	 */
	@Test
	public void testByteBufFor() {
		ByteBuf buffer = Unpooled.buffer(17).writeBytes("this is a bytebuf".getBytes());
		for (int i = 0; i < buffer.capacity(); i++) {
			System.out.print((char) buffer.getByte(i) + ",");
		}
	}

	/**
	 * 调用 {@link ByteBuf#discardReadBytes()} 以确保 可写分段的最大化，但是，这极有可能会导致内存复制，因为可读字节必须被移动到
	 * 缓冲区 的开始位置。
	 * 建议：只有在真正需要的时候才这样做，如：内存非常宝贵的时候
	 */
	@Test
	public void testByteBufDiscardReadBytes() {
		ByteBuf buffer = Unpooled.buffer(17).writeBytes("this is a bytebuf".getBytes());//t,h,i,s, ,i,s, ,a, ,b,y,t,e,b,u,f,
		buffer.readBytes(1);
		buffer.discardReadBytes();//h,i,s, ,i,s, ,a, ,b,y,t,e,b,u,f,
		for (int i = 0; i < buffer.capacity(); i++) {
			System.out.print((char) buffer.getByte(i) + ",");
		}


	}

}
