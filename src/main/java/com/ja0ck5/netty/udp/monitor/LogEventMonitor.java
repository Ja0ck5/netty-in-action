package com.ja0ck5.netty.udp.monitor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Created by Jack on 2017/9/25.
 */
public class LogEventMonitor {

	private final EventLoopGroup group;
	private final Bootstrap bootstrap;

	public LogEventMonitor(InetSocketAddress address) {
		this.group = new NioEventLoopGroup();
		this.bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioDatagramChannel.class)
				// 设置套接字选项
				.option(ChannelOption.SO_BROADCAST, true).handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						// 将 LogEventDecoder 和 LogEventHandler 添加到
						// ChannelPipeline
						pipeline.addLast(new LogEventDecoder());
						pipeline.addLast(new LogEventHandler());

					}
				}).localAddress(address);
	}

	public Channel bind() {
		// 绑定 Channel 。注意 DatagramChannel 是无连接的
		return bootstrap.bind().syncUninterruptibly().channel();
	}

	public void stop() {
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}

		LogEventMonitor logEventMonitor = new LogEventMonitor(new InetSocketAddress(Integer.parseInt(args[0])));

		try {
			Channel channel = logEventMonitor.bind();
			System.out.println("LogEventMonitor running......");
			channel.closeFuture().sync();
		} finally {
			logEventMonitor.stop();
		}

	}

}
