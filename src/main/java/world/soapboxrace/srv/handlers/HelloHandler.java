package world.soapboxrace.srv.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import world.soapboxrace.srv.protocol.FreeroamAllTalkers;
import world.soapboxrace.srv.protocol.FreeroamTalker;

public class HelloHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket) msg;
		ByteBuf buf = datagramPacket.content();
		if (isHelloPacket(buf)) {
			FreeroamTalker freeroamTalker = new FreeroamTalker(ctx, datagramPacket);
			FreeroamAllTalkers.put(freeroamTalker);
			freeroamTalker.send(welcomePacket(freeroamTalker));
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
		ctx.close();
	}

	private byte[] welcomePacket(FreeroamTalker freeroamTalker) {
		byte[] welcome = { 0x00, 0x00, 0x06, 0x01 };
		return welcome;
	}

	private boolean isHelloPacket(ByteBuf buf) {
		return (buf.getByte(2) == (byte) 0x06);
	}
}
