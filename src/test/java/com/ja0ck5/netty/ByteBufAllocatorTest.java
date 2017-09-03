package com.ja0ck5.netty;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import org.junit.Test;

/**
 * 可以通过 {@link io.netty.channel.Channel} (每个都可以有一个不同的 {@link io.netty.buffer.ByteBufAllocator} 实例)
 * <p>
 * 或者
 * <p>
 * 绑定到 {@link io.netty.channel.ChannelHandler} 的 {@link io.netty.channel.ChannelHandlerContext} 获取一个到
 * {@link io.netty.buffer.ByteBufAllocator} 的引用。
 * <p>
 * <p>
 * Netty 提供了两种 {@link io.netty.buffer.ByteBufAllocator} 实现
 * 1. {@link io.netty.buffer.PooledByteBufAllocator}
 * 池化了 ByteBuf 的实例以提高性能，并最大限度地减少内存碎片。此实现使用了一种称为 jemalloc 的已被大量现代
 * 操作系统所采用的高效方法来分配内存。
 * 2. {@link io.netty.buffer.UnpooledByteBufAllocator} 不池化 ByteBuf 实例，并且每次调用时都会返回一个新的实例。
 * <p>
 * <p>
 * <p>
 * Created by Ja0ck5 on 2017/9/3.
 */
public class ByteBufAllocatorTest {

    @Test
    public void testRefByteBufAllocator() {

    }

    @Test
    public void testByteBufUtil() {
        String hexDump = ByteBufUtil.hexDump(UnpooledByteBufAllocator.DEFAULT.buffer().writeBytes("this is a test!!!".getBytes()));
        System.out.println(hexDump);
    }

}
