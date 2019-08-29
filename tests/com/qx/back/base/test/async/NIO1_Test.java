package com.qx.back.base.test.async;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NIO1_Test {

	public static void main(String[] args) throws IOException {
		
		Selector selector = Selector.open();
		SocketChannel channel = SocketChannel.open();
		channel.register(selector, SelectionKey.OP_READ);
	}

}
