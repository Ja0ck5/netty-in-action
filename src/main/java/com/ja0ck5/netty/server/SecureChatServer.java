package com.ja0ck5.netty.server;

import com.ja0ck5.netty.initializer.SecureChatServerInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * Created by Jack on 2017/9/23.
 */
public class SecureChatServer extends ChatServer {

	private final SslContext context;

	public SecureChatServer(SslContext context) {
		this.context = context;
	}

	@Override
	protected ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
		return new SecureChatServerInitializer(channelGroup, context);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Please give port as argument");
			System.exit(1);
		}

		int port = Integer.parseInt(args[0]);
		SelfSignedCertificate cert = new SelfSignedCertificate();
		SslContextBuilder contextBuilder = SslContextBuilder.forServer(cert.certificate(), cert.privateKey());

		final SecureChatServer endpoint = new SecureChatServer(contextBuilder.build());
		ChannelFuture future = endpoint.start(new InetSocketAddress(port));
		Runtime.getRuntime().addShutdownHook(new Thread(() -> endpoint.destroy()));
		future.channel().closeFuture().syncUninterruptibly();
	}

}
