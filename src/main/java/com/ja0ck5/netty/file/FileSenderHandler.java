package com.ja0ck5.netty.file;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.File;
import java.io.FileInputStream;

/**
 * 零拷贝特性，消除了将文件的内容从文件系统移动到网络栈的复制过程。
 * {@link FileRegion} 通过支持零拷贝的文件传输的 Channel 来发送的文件区域
 *
 * 这个示例只适用于文件内容的直接传输，不包括应用程序对数据的任何处理。
 * 在需要将数据从文件系统复制到用户内存中时，可以使用 {@link io.netty.handler.stream.ChunkedWriteHandler}
 * Created by Jack on 2017/9/21.
 */
public class FileSenderHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		File file = new File("");
		FileInputStream in = new FileInputStream(file);
		// 以该文件完整的长度创建一个新的 DefaultFileRegion
		DefaultFileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
		// 发送 DefaultFileRegion 并注册监听
		ctx.channel().writeAndFlush(region).addListener((ChannelFutureListener) future -> {
			if (!future.isSuccess()) {
				Throwable cause = future.cause();
				// handler cause
			}
		});
	}
}
