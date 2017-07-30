package world.soapboxrace.srv.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import world.soapboxrace.srv.protocol.FreeroamAllTalkers;
import world.soapboxrace.srv.protocol.FreeroamTalker;

public class PlayerInfoHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket) msg;
		ByteBuf buf = datagramPacket.content();
		if (isPlayerInfoPacket(buf)) {
			FreeroamTalker freeroamTalker = FreeroamAllTalkers.get(datagramPacket);
			if (freeroamTalker != null) {
				freeroamTalker.parsePlayerInfo(ByteBufUtil.getBytes(buf));
			}
		}
		super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		System.err.println(cause.getMessage());
		ctx.close();
	}

	private boolean isPlayerInfoPacket(ByteBuf buf) {
		return (buf.getByte(2) == (byte) 0x07);
	}
}
