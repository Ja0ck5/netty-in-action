package com.ja0ck5.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {
	private final EventLoopGroup group;

	private final Bootstrap bootstrap;

	private final File file;

	public LogEventBroadcaster(InetSocketAddress address, File file) {
		this.group = new NioEventLoopGroup();
		this.bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
				.handler(new LogEventEncoder(address));
		this.file = file;
	}

	public void start() throws Exception {
		Channel ch = bootstrap.bind(0).sync().channel();
		long pointer = 0;
		for (;;) {
			long len = file.length();
			if (len < pointer) {
				// file was reset
				// 如果有必要，将文件指针设置到该文件的最后一个字节
				pointer = len;
			} else if (len > pointer) {
				// content was add
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				// 设置当前的文件指针，以确保没有任何旧日志被发送
				raf.seek(pointer);
				String line;
				while ((line = raf.readLine()) != null) {
					// 对于每个日志条目，写入一个 LogEvent 到 Channel 中
					ch.writeAndFlush(new LogEvent(file.getAbsolutePath(), line));
				}
				// 存储其再文件中的当前位置
				pointer = raf.getFilePointer();
				raf.close();
			}

			// 休眠一秒如果被中断则退出循环
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
		}
	}

	public void stop() {
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			throw new IllegalArgumentException();
		}
		LogEventBroadcaster broadcaster = new LogEventBroadcaster(
				new InetSocketAddress("255.255.255.255", Integer.parseInt(args[0])), new File(args[1]));
		try {
			broadcaster.start();
		} finally {
			broadcaster.stop();
		}
	}

}
