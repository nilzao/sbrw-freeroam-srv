package world.soapboxrace.srv.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import world.soapboxrace.srv.handlers.HelloHandler;
import world.soapboxrace.srv.handlers.PlayerInfoHandler;

public class ServerChannelInitializer extends ChannelInitializer<DatagramChannel> {
	@Override
	protected void initChannel(DatagramChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("hello", new HelloHandler());
		pipeline.addLast("playerInfo", new PlayerInfoHandler());
	}
}
