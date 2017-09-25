package com.ja0ck5.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {
    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true).handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void start() throws Exception {
        Channel ch = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        for (; ; ) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len;
            }

        }
    }

}
