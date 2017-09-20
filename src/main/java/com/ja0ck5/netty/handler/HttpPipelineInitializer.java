package com.ja0ck5.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 如果是客户端，则添加 HttpResponseDecoder handler response from server
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("decoder", new HttpResponseDecoder());
            // if client, add httpRequestEncoder for sending request for server
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            // if server ,add HttpRequestDecoder for accepting request of client
            pipeline.addLast("decoder", new HttpRequestDecoder());
            // HttpResponseEncoder for sending response to client
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
