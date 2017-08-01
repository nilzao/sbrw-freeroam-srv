package world.soapboxrace.srv.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import world.soapboxrace.debug.UdpDebug;

public class ExampleHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DatagramPacket datagramPacket = (DatagramPacket) msg;
		ByteBuf buf = datagramPacket.content();
		byte[] bytes = ByteBufUtil.getBytes(buf);
		// System.out.println("receiving [" + UdpDebug.byteArrayToHexString(bytes) + "]");
		super.channelRead(ctx, msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause.getMessage());
		ctx.close();
	}

}
