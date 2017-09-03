package com.ja0ck5.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by Jack on 2017/8/26.
 */
public class ByteBufTest {

    /**
     * 使用一些 需要 “一个索引值参数的方法” 之一来访问数据，既不会改变 readerIndex,也不会改变 writerIndex 。
     * 如果有需要也可以通过调用 {@link ByteBuf#readerIndex(int)} or
     * {@link ByteBuf#writerIndex(int)} 来手动移动
     * <p>
     * <p>
     * {@link ByteBuf} 具有读索引和写索引，但是 jdk {@link java.nio.ByteBuffer} 只有一个索引，也就是为什么需要调用{@link ByteBuffer#flip()}
     * 方法来在 读模式 和 写模式在之间进行切换的原因。
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
     * 可丢弃字节
     * <p>
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


    /**
     * {@link ByteBuf} 的可读字节分段存储了实际数据。新分配的、包装的或者复制的缓冲区的默认
     * readerIndex 值 为 0；任何名称 以 read or skip 开头的操作豆浆检索或者跳过位于当前的
     * readerIndex 的数据，并且将它增加已读字节数。
     * <p>
     * 如果被调用的方法需要一个 {@link ByteBuf}  参数作为写入目标。并且没有指定的目标索引参数，
     * 那么 该目标缓冲区的 writerIndex 也将被增加，例如
     * {@link ByteBuf#readBytes(ByteBuf)}
     * <p>
     * 如果尝试在缓冲区的刻度字节数已经耗尽时 从中读取数据，那么将会引发一个 IndexOutOfBoundsException
     */
    @Test
    public void testByteBufReadByte() {
        ByteBuf buffer = Unpooled.buffer(17).writeBytes("this is a bytebuf".getBytes());//t,h,i,s, ,i,s, ,a, ,b,y,t,e,b,u,f,
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    /**
     * 定义：
     * 可写字节分段是指一个拥有未定义内容的、写入就绪的内存区域。
     * <p>
     * 新分配的缓冲区的 writerIndex 的默认值为 0； 任何名称为 write 开头的操作都将从前的 writerIndex
     * 位置开始写数据，并将它增加已写入的字节数。
     * <p>
     * 如果写操作的目标是 {@link ByteBuf} ,并且没有指定源索引的值，则源缓冲区的 readerIndex 也被增加 相同的大小
     * <p>
     * 如：
     * {@link ByteBuf#writeBytes(ByteBuf)}
     * <p>
     * 如果尝试往目标写入超过目标容量的数据，那么将会引发一个 IndexOutOfBoundsException
     */
    @Test
    public void testByteBufWriteByte() {
        ByteBuf buffer = Unpooled.buffer(17).writeBytes("this is a bytebuf".getBytes());//t,h,i,s, ,i,s, ,a, ,b,y,t,e,b,u,f,
        while (buffer.writableBytes() >= 4) {
            // 使用随机数填充缓冲区，直到空间不足为止
            buffer.writeInt(new Random(10).nextInt());
            System.out.println(buffer);
        }
    }

    /**
     * 查找
     */
    @Test
    public void testByteProcessor() {
        ByteBuf byteBuf = Unpooled.buffer(20).writeBytes("this is a bytebuf\r".getBytes());
        int i = byteBuf.forEachByte(ByteProcessor.FIND_CR);
        System.out.println(i);
    }

    /**
     * 派生缓冲区
     * {@link ByteBuf#duplicate()}
     * {@link ByteBuf#slice()}
     * {@link ByteBuf#slice(int, int)}
     * {@link ByteBuf#order()}
     * {@link ByteBuf#readSlice(int)}
     * {@link Unpooled#unmodifiableBuffer(ByteBuf...)}
     * <p>
     * 每个方法豆浆返回一个新的 ByteBuf 实例，它具有自己的读索引/写索引和标记索引
     * 其内部存储和 JDK 的{@link ByteBuffer} 一样，也是共享的。
     * 这使得派生缓冲区的创建成本是很低廉的，但是也意味着，如果你修改了它的内容，也同时修改了其对应的
     * 源实例。
     * <p>
     * ByteBuf 的复制：
     * 如果需要一个现有的缓冲区真实的副本，使用 {@link ByteBuf#copy()}
     * 不同于 派生缓冲区，由此调用返回的 ByteBuf 拥有独立的数据副本
     */
    @Test
    public void testByteSlice() {
        // 切片
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!!!", utf8);
        // 创建该 ByteBuf 从索引0 - 15 结束的一个新切片
        ByteBuf slice = byteBuf.slice(0, 15);
        System.out.println(slice.toString(utf8));
        // 更新索引0 的字节
        byteBuf.setByte(0, 'J');
        // true,因为数据是共享的，对其中一个所做的更改对另一个也可见
        Assert.assertTrue(byteBuf.getByte(0) == slice.getByte(0));
    }

    @Test
    public void testByteCopy() {
        // 切片
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!!!", utf8);
        // 创建该 ByteBuf 从索引0 - 15 结束的一个新切片
        ByteBuf copy = byteBuf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        // 更新索引0 的字节
        byteBuf.setByte(0, 'J');
        // true,因为数据不是是共享的，对其中一个所做的更改对另一个不可见
        Assert.assertTrue(byteBuf.getByte(0) != copy.getByte(0));
    }

    @Test
    public void testByteGetSet() {
        // 切片
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!!!", utf8);
        System.out.println(byteBuf.getByte(0));
        // 存储当前的 readerIndex/ writerIndex
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        // 更新索引0 的字节
        byteBuf.setByte(0, 'B');
        Assert.assertTrue(readerIndex == byteBuf.readerIndex());
        Assert.assertTrue(writerIndex == byteBuf.writerIndex());
    }

    @Test
    public void testByteReadWrite() {
        // 切片
        Charset utf8 = Charset.forName("UTF-8");
        // 创建一个用于保存给定字符串的 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!!!", utf8);
        // 存储当前的 readerIndex/ writerIndex
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();
        System.out.println(byteBuf.readByte());
        // 写入字节
        byteBuf.writeByte('?');
        Assert.assertTrue(readerIndex != byteBuf.readerIndex());
        Assert.assertTrue(writerIndex != byteBuf.writerIndex());
    }

}
