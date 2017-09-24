package com.ja0ck5.netty.udp;

import java.net.InetSocketAddress;

/**
 * Created by Jack on 2017/9/23.
 */
public class LogEvent {

	public static final byte SEPARATOR = ':';

	private final InetSocketAddress source;

	private final String logfile;

	private final String msg;

	private final long received;

	public LogEvent(String logfile, String msg) {
		this(null, logfile, msg, -1);
	}

	public LogEvent(InetSocketAddress source, String logfile, String msg, long received) {
		this.source = source;
		this.logfile = logfile;
		this.msg = msg;
		this.received = received;
	}

	/**
	 * @return the {@link #SEPARATOR}
	 */
	public static byte getSEPARATOR() {
		return SEPARATOR;
	}

	/**
	 * @return the {@link #source}
	 */
	public InetSocketAddress getSource() {
		return source;
	}

	/**
	 * @return the {@link #logfile}
	 */
	public String getLogfile() {
		return logfile;
	}

	/**
	 * @return the {@link #msg}
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return the {@link #received}
	 */
	public long getReceived() {
		return received;
	}
}
