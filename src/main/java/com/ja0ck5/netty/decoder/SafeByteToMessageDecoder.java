package com.ja0ck5.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * Created by Ja0ck5 on 2017/9/17.
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readable = byteBuf.readableBytes();
        if (readable > MAX_FRAME_SIZE) {
            // 跳出所有可读字节，抛出 TooLongFrameException 并通知 ChannelHandler
            byteBuf.skipBytes(readable);
            throw new TooLongFrameException();
        }
    }
}
