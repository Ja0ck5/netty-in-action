package com.ja0ck5.netty.server;

import java.net.InetSocketAddress;

import com.ja0ck5.netty.initializer.ChatServerInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * Created by Jack on 2017/9/23.
 */
public class ChatServer {
	/**
	 * 创建 DefaultChannelGroup 其将保存所有已经连接的 WebSocket Channel
	 */
	private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

	private EventLoopGroup group = new NioEventLoopGroup();

	private Channel channel;

	public ChannelFuture start(InetSocketAddress address) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group).channel(NioServerSocketChannel.class).childHandler(createInitializer(channelGroup));
		ChannelFuture future = bootstrap.bind(address);
		future.syncUninterruptibly();
		channel = future.channel();
		return future;
	}

	private ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
		return new ChatServerInitializer(channelGroup);
	}

	/**
	 * 处理服务器关闭，并释放所有资源
	 */
	public void destroy() {
		if (null != channel) {
			channel.close();
		}
		channelGroup.close();
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Please give port as argument");
			System.exit(1);
		}

		int port = Integer.parseInt(args[0]);
		final ChatServer endpoint = new ChatServer();
		ChannelFuture future = endpoint.start(new InetSocketAddress(port));
		Runtime.getRuntime().addShutdownHook(new Thread(() -> endpoint.destroy()));
		future.channel().closeFuture().syncUninterruptibly();

	}

}
