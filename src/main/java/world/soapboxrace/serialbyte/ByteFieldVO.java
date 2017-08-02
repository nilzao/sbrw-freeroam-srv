package world.soapboxrace.serialbyte;

import java.lang.reflect.Field;

public class ByteFieldVO implements Comparable<ByteFieldVO> {

	private int start;
	private Field field;

	public ByteFieldVO(Field field) {
		this.field = field;
		start = field.getAnnotation(ByteField.class).start();
	}

	@Override
	public int compareTo(ByteFieldVO o) {
		if (this.start >= o.start) {
			return 1;
		}
		return -1;
	}

	public Field getField() {
		return field;
	}

	public int getStart() {
		return start;
	}

	public int getSize() {
		return field.getAnnotation(ByteField.class).size();
	}

}
