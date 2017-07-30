package world.soapboxrace.srv.protocol;

import java.util.HashMap;

import io.netty.channel.socket.DatagramPacket;

public class FreeroamAllTalkers {

	private static HashMap<Integer, FreeroamTalker> freeroamTalkers = new HashMap<>();

	public static void put(FreeroamTalker freeroamTalker) {
		freeroamTalkers.put(freeroamTalker.getPort(), freeroamTalker);
	}

	public static FreeroamTalker get(DatagramPacket datagramPacket) {
		return freeroamTalkers.get(datagramPacket.sender().getPort());
	}

	public static HashMap<Integer, FreeroamTalker> getFreeroamTalkers() {
		return freeroamTalkers;
	}

}
