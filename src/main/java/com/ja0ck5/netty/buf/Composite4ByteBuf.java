package com.ja0ck5.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
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
