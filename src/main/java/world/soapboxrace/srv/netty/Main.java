package world.soapboxrace.srv.netty;

import io.netty.channel.ChannelFuture;
import world.soapboxrace.srv.protocol.Sender;

public class Main {

	public static void main(String[] args) {
		int port = 9999;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}
		new Sender();
		NettyUdpServer server;
		try {
			server = new NettyUdpServer(port);
			ChannelFuture future = server.start();
			// Wait until the connection is closed.
			future.channel().closeFuture().sync();
		} catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
	}

}
