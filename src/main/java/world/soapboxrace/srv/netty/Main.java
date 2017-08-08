package world.soapboxrace.srv.netty;

import io.netty.channel.ChannelFuture;
import world.soapboxrace.srv.protocol.Sender;
import world.soapboxrace.srv.protocol.SenderInfo;

public class Main {

	public static void main(String[] args) {
		int port = 9999;
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}
		new Sender();
		new SenderInfo();
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
