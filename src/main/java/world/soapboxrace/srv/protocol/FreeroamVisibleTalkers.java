package world.soapboxrace.srv.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FreeroamVisibleTalkers {

	private List<FreeroamTalker> visibleTalkers;

	private int limit = 3;

	public FreeroamVisibleTalkers() {
		visibleTalkers = new ArrayList<>(limit);
		for (int i = 0; i < limit; i++) {
			visibleTalkers.add(null);
		}
	}

	public void broadcastPlayersXYZToTalker(FreeroamTalker freeroamTalker) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
		int size = 0;
		for (int i = 0; i < visibleTalkers.size(); i++) {
			byteBuffer.put((byte) 0x00);
			FreeroamTalker freeroamTalkerTmp = visibleTalkers.get(i);
			if (freeroamTalkerTmp != null && freeroamTalkerTmp.isReady()) {
				byte[] playerPacket = visibleTalkers.get(i).getPlayerXYZ();
				if (playerPacket != null) {
					size = size + playerPacket.length;
					byteBuffer.put(playerPacket);
					playerPacket = null;
				}
			}
			byteBuffer.put((byte) 0xff);
			size = size + 2;
		}
		byte[] byteTmpReturn = new byte[size];
		System.arraycopy(byteBuffer.array(), 0, byteTmpReturn, 0, byteTmpReturn.length);
		freeroamTalker.sendFullPacket(byteTmpReturn);
		byteTmpReturn = null;
	}

	public void broadcastPlayersInfoToTalker(FreeroamTalker freeroamTalker) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
		int size = 0;
		for (int i = 0; i < limit; i++) {
			byteBuffer.put((byte) 0x00);
			FreeroamTalker freeroamTalkerTmp = visibleTalkers.get(i);
			if (freeroamTalkerTmp != null && freeroamTalkerTmp.isReady()) {
				byte[] playerPacket = freeroamTalkerTmp.getPlayerInfo();
				if (playerPacket != null) {
					size = size + playerPacket.length;
					byteBuffer.put(playerPacket);
					playerPacket = null;
				}
			}
			byteBuffer.put((byte) 0xff);
			size = size + 2;
		}
		byte[] byteTmpReturn = new byte[size];
		System.arraycopy(byteBuffer.array(), 0, byteTmpReturn, 0, byteTmpReturn.length);
		freeroamTalker.sendFullPacket(byteTmpReturn);
	}

	public void addVisibleTalkerToTalker(FreeroamTalker newFreeroamTalker, FreeroamTalker freeroamTalker) {
		int indexOf = visibleTalkers.indexOf(newFreeroamTalker);
		if (indexOf == -1) {
			addTalker(newFreeroamTalker, freeroamTalker);
		}
	}

	private void addTalker(FreeroamTalker newFreeroamTalker, FreeroamTalker freeroamTalker) {
		for (int i = 0; i < limit; i++) {
			if (visibleTalkers.get(i) == null) {
				visibleTalkers.add(i, newFreeroamTalker);
				freeroamTalker.broadcastPlayersInfo();
				return;
			}
		}
	}

	public void removeVisibleTalkerToTalker(FreeroamTalker freeroamTalker) {
		for (int i = 0; i < limit; i++) {
			FreeroamTalker idleFreeroamTalkerTmp = visibleTalkers.get(i);
			if (idleFreeroamTalkerTmp != null && !idleFreeroamTalkerTmp.isAlive()) {
				System.out.println("remove from visibility");
				visibleTalkers.set(i, null);
			}
		}
	}
}
