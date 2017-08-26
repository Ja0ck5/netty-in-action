package com.ja0ck5.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 在发送数据的时候，传统的实现方式是：
 * <p>
 * File.read(bytes)
 * Socket.send(bytes)
 * 这种方式需要四次数据拷贝和四次上下文切换：
 * 1. 数据从磁盘读取到内核的read buffer
 * 2. 数据从内核缓冲区拷贝到用户缓冲区
 * 3. 数据从用户缓冲区拷贝到内核的socket buffer
 * 4. 数据从内核的socket buffer拷贝到网卡接口的缓冲区
 * <p>
 * 明显上面的第二步和第三步是没有必要的，通过java的FileChannel.transferTo方法，可以避免上面两次多余的拷贝（当然这需要底层操作系统支持）
 * 1. 调用transferTo,数据从文件由DMA引擎拷贝到内核read buffer
 * 2. 接着DMA从内核read buffer将数据拷贝到网卡接口buffer
 * <p>
 * 上面的两次操作都不需要CPU参与，所以就达到了零拷贝。
 * <p>
 * <p>
 * Netty的零拷贝体现在三个方面：
 * 1. Netty的接收和发送ByteBuffer采用DIRECT BUFFERS，使用堆外直接内存进行Socket读写，不需要进行字节缓冲区的二次拷贝。如果使用传统的堆内存（HEAP BUFFERS）进行Socket读写，JVM会将堆内存Buffer拷贝一份到直接内存中，然后才写入Socket中。相比于堆外直接内存，消息在发送过程中多了一次缓冲区的内存拷贝。
 * 2. Netty提供了组合Buffer对象，可以聚合多个ByteBuffer对象，用户可以像操作一个Buffer那样方便的对组合Buffer进行操作，避免了传统通过内存拷贝的方式将几个小Buffer合并成一个大的Buffer。
 * 3. Netty的文件传输采用了transferTo方法，它可以直接将文件缓冲区的数据发送到目标Channel，避免了传统通过循环write方式导致的内存拷贝问题。
 * Created by Ja0ck5 on 2017/8/25.
 */
public class Composite4ByteBuf {

    public static void main(String[] args) {
        // 得到未池化的 符合缓冲区
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        //
        ByteBuf headerBuf = Unpooled.buffer(128);
        headerBuf.writeBytes("this is headerBuf".getBytes());
        ByteBuf bodyBuf = Unpooled.buffer(128);
        bodyBuf.writeBytes("this is bodyBuf ".getBytes());

        messageBuf.addComponent(headerBuf).addComponent(bodyBuf);
        // 删除位于索引位置为 0 的 ByteBuf
//        messageBuf.removeComponent(0);
        messageBuf.forEach(System.out::println);

        /**
         *  {@link CompositeByteBuf} 可能不支持访问 其支撑数组，因此访问 {@link CompositeByteBuf} 的数据类似于
         *  访问 直接缓冲区的模式
         */
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int len = compBuf.readableBytes();
        byte[] array = new byte[len];
        compBuf.getBytes(compBuf.readerIndex(), array);
        // handleArray(array,0,array.length); 使用偏移量和长度作为参数使用该数组

    }


}
