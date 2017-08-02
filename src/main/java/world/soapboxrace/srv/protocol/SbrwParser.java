package world.soapboxrace.srv.protocol;

import java.nio.ByteBuffer;

public class SbrwParser {

	private byte[] playerInfo;

	private CarProtocol carProtocol = new CarProtocol();

	public SbrwParser(byte[] playerInfo) {
		parseInputData(playerInfo);
	}

	public void parseInputData(byte[] playerInfo) {
		ByteBuffer byteBuff = ByteBuffer.allocate(6);
		byteBuff.put(playerInfo, 3, 6);
		carProtocol.deserialize(byteBuff.array());
		this.playerInfo = byteBuff.array();
	}

	public byte[] getPlayerPacket() {
		return this.playerInfo;
	}

	public byte[] getStatePosPacket() {
		ByteBuffer byteBuff = ByteBuffer.allocate(4);
		byteBuff.putShort(carProtocol.getX());
		byteBuff.putShort(carProtocol.getY());
		return byteBuff.array();
	}

	public int getXPos() {
		return Short.toUnsignedInt(carProtocol.getX());
	}

	public int getYPos() {
		return Short.toUnsignedInt(carProtocol.getY());
	}

	public int getPlayerId() {
		return carProtocol.getPlayerId();
	}

}
