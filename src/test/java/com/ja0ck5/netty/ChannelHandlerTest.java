package com.ja0ck5.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * 
 * {@link io.netty.channel.ChannelInboundHandler} 处理入栈数据以及各种状态的变化 它的实现重写了
 * {@link io.netty.channel.ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}
 * 时， 它将负责 显式 地释放与池化的 {@link io.netty.buffer.ByteBuf} 实例相关的内存。实用方法
 * {@link io.netty.util.ReferenceCountUtil#release(Object)} 用于 discard 已经接收得消息。
 * 但是以这种方式管理资源可能会很繁琐，更简单的方式是使用
 * {@link io.netty.channel.SimpleChannelInboundHandler}
 * 它会自动释放资源，所以不应该存储任何消息的引用供将来使用，因为这些引用都将会失效。
 *
 * {@link io.netty.channel.ChannelOutboundHandler} 处理出站数据并且允许拦截所有操作
 * 一个强大的功能是可以按需推迟操作或者事件，是的可以通过一些复杂的方法来处理请求。例如： 远程节点的写入被暂停了， 那么可以推迟冲刷操作并在稍后继续。
 * 
 * {@link io.netty.channel.ChannelPromise} 与
 * {@link io.netty.channel.ChannelFuture}
 * {@link io.netty.channel.ChannelOutboundHandler} 中的大部分方法都需要一个 ChannelPromise
 * 参数， 以便在操作完成时得到通知。 ChannelPromise 是 {@link io.netty.channel.ChannelFuture}
 * 的一个子类， 其定义了一些可写的方法，如
 * {@link ChannelPromise#setSuccess()}/{@link ChannelPromise#setFailure(Throwable)},
 * 从而使 ChannelFuture 不可变。
 * 
 * 这里借鉴的是 Scala 的 Promise 和 Future 的设计，当一个 Promise 被完成之后，其对应的 Future
 * 的值便不能再进行任何修改。
 *
 *
 * 
 * Created by Jack on 2017/9/4.
 */
public class ChannelHandlerTest {
}
