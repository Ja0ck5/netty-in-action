package com.ja0ck5.netty.initializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Created by Jack on 2017/9/21.
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {
	final static byte SPACE = ' ';

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 添加 CmdDecoder 以提取 Cmd 对象，并将它转发给下一个 ChannelInboundHandler
		pipeline.addLast(new CmdDecoder(1 << 16));
		// 添加 CmdHandler 以接收和处理 Cmd 对象
		pipeline.addLast(new CmdHandler());
	}

	/**
	 * Cmd POJO
	 */
	public static final class Cmd {
		private final ByteBuf name;
		private final ByteBuf args;

		public Cmd(ByteBuf name, ByteBuf args) {
			this.name = name;
			this.args = args;
		}

		/**
		 * @return the {@link #name}
		 */
		public ByteBuf name() {
			return name;
		}

		/**
		 * @return the {@link #args}
		 */
		public ByteBuf args() {
			return args;
		}
	}

	public static final class CmdDecoder extends LineBasedFrameDecoder {

		public CmdDecoder(int maxLength) {
			super(maxLength);
		}

		@Override
		protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
			// 从 ByteBuf 中提取有行尾分隔符序列分隔的帧
			ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
			if (null == frame) {
				return null;
			}
			// 查找第一个空格字符 的索引
			int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
			// 使用包含 Cmd 名称和参数的切片创建新的 Cmd 对象
			return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
		}
	}

	public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
			// 处理传经 ChannelPipeline 的 Cmd 对象
			System.out.println(msg.name() + "args : " + msg.args());
		}
	}
}
