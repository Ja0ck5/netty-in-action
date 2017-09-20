package com.ja0ck5.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (isClient) {
            // if client,add HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // if server
            pipeline.addLast("codec", new HttpServerCodec());
        }
        // add HttpObjectAggregator which size is 512KB
        pipeline.addLast("aggregator", new HttpObjectAggregator((2 << 9) * (2 << 10)));
    }
}
