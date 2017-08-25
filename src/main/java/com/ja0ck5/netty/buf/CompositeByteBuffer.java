package com.ja0ck5.netty.buf;

import java.nio.ByteBuffer;

/**
 * 使用 {@link java.nio.ByteBuffer} 的复合缓冲区模式
 * <p>
 * 分配和复制操作，以及伴随着对数组管理的需要，使得这个版本的实现效率低下而且笨拙。
 * <p>
 * </p>
 * Created by Ja0ck5 on 2017/8/25.
 */
public class CompositeByteBuffer {

    public static void main(String[] args) {
        ByteBuffer header = ByteBuffer.allocate(128);
        header.put("this is header".getBytes());
        header.flip();
        ByteBuffer body = ByteBuffer.allocate(128);
        body.put("this is body".getBytes());
        body.flip();
        // use an array to hold the message parts
        ByteBuffer[] message = new ByteBuffer[]{header, body};
        // create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
        System.out.println(message2);
    }

}
