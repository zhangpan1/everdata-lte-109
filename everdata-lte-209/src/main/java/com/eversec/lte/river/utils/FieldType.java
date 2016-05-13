package com.eversec.lte.river.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@SuppressWarnings("rawtypes") 
public class FieldType {
	String name;
	Class<?> type;
	Field field;

	public FieldType(String name, Class<?> type,Field field) {
		this.name = name;
		this.type = type;
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField(Field field) {
		this.field = field;
	}

	public static FieldType[] toArray(Field[] fs) {
		FieldType[] result = new FieldType[fs.length];
		for (int i = 0; i < fs.length; i++) {
			result[i] = new FieldType(fs[i].getName(), fs[i].getType(),fs[i]);
		}
		return result;
	}

	public static List<FieldType> toList(Field[] fs) {
		return Arrays.asList(toArray(fs));
	}

	public static FieldType[] toArray(Class class1) {
		return toArray(class1.getDeclaredFields());
	}

	public static List<FieldType> toList(Class class1) {
		return Arrays.asList(toArray(class1));
	}

	public static List<FieldType> toList(Object... obj) {
		List<FieldType> result = new ArrayList<FieldType>();
		for (Object o : obj) {
			if (o instanceof Class) {
				result.addAll(toList((Class) o));
			} else if (o instanceof Field[]) {
				result.addAll(toList((Field[]) o));
			} else if (o instanceof FieldType) {
				result.add((FieldType) o);
			} else if (o instanceof FieldType[]) {
				result.addAll(Arrays.asList((FieldType[]) o));
			}
		}
		return result;
	}

	public static FieldType[] toArray(Object... obj) {
		return toList(obj).toArray(new FieldType[0]);
	}

	public static FieldType[] removeValues(FieldType[] fs1, String... string) {
		if (string == null || string.length == 0) {
			throw new NullPointerException();
		}
		ArrayList<FieldType> result = new ArrayList<FieldType>();
		Outter: for (int i = 0; i < fs1.length; i++) {
			for (int j = 0; j < string.length; j++) {
				String str = string[j];
				if (fs1[i].getName().equals(str)) {
					continue Outter;
				}
			}
			result.add(fs1[i]);
		}
		return result.toArray(new FieldType[0]);
	}

	public static List<FieldType> removeValues(List<FieldType> fs1,
			String... string) {
		return Arrays
				.asList(removeValues(fs1.toArray(new FieldType[0]), string));
	}

}
