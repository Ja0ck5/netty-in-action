package com.ja0ck5.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * Created by Ja0ck5 on 2017/9/18.
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext sslContext;

    private final boolean startTls;

    public SslChannelInitializer(SslContext sslContext, boolean startTls) {
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        SSLEngine engine = sslContext.newEngine(channel.alloc());// 对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 sslContext 获取一个 新的 SSLEngine
        // 将 SSLhandler 作为第一个 ChannelHandler 添加到ChannelPipeline 中
        channel.pipeline().addLast("ssl", new SslHandler(engine, startTls));
    }
}
