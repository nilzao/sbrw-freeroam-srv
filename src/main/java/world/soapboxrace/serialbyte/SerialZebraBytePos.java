package world.soapboxrace.serialbyte;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerialZebraBytePos {

	private static final int BUFFER_LIMIT = 2048;

	private List<ByteFieldVO> fields;

	public byte[] serialize() throws Exception {
		loadFields();
		ByteBuffer byteArrayBuffer = ByteBuffer.allocate(BUFFER_LIMIT);
		int lenght = 0;
		for (ByteFieldVO byteFieldVO : fields) {
			Field field = byteFieldVO.getField();
			byte[] fieldValueToByteArray = fieldValueToByteArray(field);
			if (fieldValueToByteArray != null) {
				byteArrayBuffer.put(fieldValueToByteArray, 0, fieldValueToByteArray.length);
				lenght = lenght + fieldValueToByteArray.length;
			}
		}
		byte[] byteTmp = new byte[lenght];
		System.arraycopy(byteArrayBuffer.array(), 0, byteTmp, 0, lenght);
		return byteTmp;
	}

	private byte[] fieldValueToByteArray(Field field) throws Exception {
		byte[] byteTmp = null;
		ByteField annotation = field.getAnnotation(ByteField.class);
		Object objectTmp = field.get(this);
		if (objectTmp instanceof String) {
			return stringToByteArray(annotation, (String) objectTmp);
		} else if (objectTmp instanceof Integer) {
			return intToByteArray((Integer) objectTmp);
		} else if (field.getType() == byte[].class) {
			return byteArrayToByteArray(annotation, (byte[]) objectTmp);
		} else if (objectTmp instanceof Short) {
			return shortToByteArray((short) objectTmp);
		} else if (objectTmp instanceof Byte) {
			return new byte[] { (byte) objectTmp };
		}
		return byteTmp;
	}

	private byte[] byteArrayToByteArray(ByteField annotation, byte[] objectTmp) {
		int size = annotation.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		byteBuffer.put(objectTmp);
		return byteBuffer.array();
	}

	private byte[] stringToByteArray(ByteField annotation, String stringTmp) {
		int size = annotation.size();
		ByteBuffer byteBuffer = ByteBuffer.allocate(annotation.size());
		byte[] stringBytes = stringTmp.getBytes();
		if (stringBytes.length <= size) {
			byteBuffer.put(stringBytes);
		} else {
			byte[] byteTmp = new byte[size];
			System.arraycopy(stringBytes, 0, byteTmp, 0, byteTmp.length);
			byteBuffer.put(byteTmp);
		}
		return byteBuffer.array();
	}

	private byte[] shortToByteArray(short shortTmp) {
		return ByteBuffer.allocate(2).putShort(shortTmp).array();
	}

	private byte[] intToByteArray(Integer intTmp) {
		return ByteBuffer.allocate(4).putInt(intTmp).array();
	}

	private void loadFields() {
		fields = new ArrayList<>();
		Field[] declaredFields = this.getClass().getDeclaredFields();
		Field.setAccessible(declaredFields, Boolean.TRUE);
		for (Field field : declaredFields) {
			fields.add(new ByteFieldVO(field));
		}
		Collections.sort(fields);
	}

	public void deserialize(byte[] data) throws Exception {
		if (data != null && data.length > 0) {
			loadFields();
			for (ByteFieldVO byteFieldVO : fields) {
				parseDataBytes(data, byteFieldVO);
			}
		}
	}

	private void parseDataBytes(byte[] data, ByteFieldVO byteFieldVO) throws Exception {
		Field field = byteFieldVO.getField();
		int start = byteFieldVO.getStart();
		Class<?> type = field.getType();
		if (type.isAssignableFrom(String.class)) {
			int byteTmpSize = byteFieldVO.getSize();
			byte[] bytesTmp = new byte[byteTmpSize];
			System.arraycopy(data, start, bytesTmp, 0, byteTmpSize);
			String string = new String(bytesTmp);
			field.set(this, string);
		} else if (type.isAssignableFrom(Integer.class) || type == int.class) {
			byte[] bytesTmp = new byte[4];
			System.arraycopy(data, start, bytesTmp, 0, 4);
			ByteBuffer wrapped = ByteBuffer.wrap(bytesTmp);
			int int1 = wrapped.getInt();
			field.set(this, int1);
		} else if (type == byte[].class) {
			int byteTmpSize = byteFieldVO.getSize();
			byte[] bytesTmp = new byte[byteTmpSize];
			System.arraycopy(data, start, bytesTmp, 0, byteTmpSize);
			field.set(this, bytesTmp);
		} else if (type.isAssignableFrom(Short.class) || type == short.class) {
			byte[] bytesTmp = new byte[2];
			System.arraycopy(data, start, bytesTmp, 0, 2);
			ByteBuffer wrapped = ByteBuffer.wrap(bytesTmp);
			short short1 = wrapped.getShort();
			field.set(this, short1);
		} else if (type.isAssignableFrom(Byte.class) || type == byte.class) {
			byte byteTmp = data[start];
			field.set(this, byteTmp);
		}
	}

}
