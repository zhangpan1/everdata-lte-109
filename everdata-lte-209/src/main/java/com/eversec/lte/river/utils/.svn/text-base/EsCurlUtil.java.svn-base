package com.eversec.lte.river.utils;

@SuppressWarnings("rawtypes")
public abstract class EsCurlUtil {
	public String formatJson(String json) {
		return JsonFormatTool.formatJson(json, "\t");
	}

	// TODO
	public FieldType[] createClazzFields(Class clazz) {
		return FieldType.toArray(clazz.getDeclaredFields());
	}

	public String createCommon(Class<?> clazz) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(clazz.getSimpleName());
		sb.append("\t");
		FieldType[] fs = createClazzFields(clazz);
		for (int i = 0; i < fs.length; i++) {
			FieldType f = fs[i];
			sb.append(f.getName());
			sb.append(", ");
		}
		return sb.toString();
	}

	public String createTypeName(Class<?> clazz) {
		if(clazz.isAnnotationPresent(EsMetaData.class) ){
			EsMetaData es = clazz.getAnnotation(EsMetaData.class);
			if(es != null && es.classIndexName().length()>0){
				return es.classIndexName();
			}
		}
		String name = clazz.getSimpleName();
		StringBuilder sb = new StringBuilder();
		char[] chars = name.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (i == 0) {
				sb.append(Character.toLowerCase(c));
			} else if (Character.isUpperCase(c)) {
				if (i - 1 >= 0
						&& (Character.isUpperCase(chars[i - 1]) || Character
								.isDigit(chars[i - 1]))) {// 排除连续
				} else {
					sb.append('_');
				}
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public EsMetaDataInfo getMetaDataInfo(FieldType fieldType) {
		if (fieldType.getField()!= null && fieldType.getField().isAnnotationPresent(EsMetaData.class)) {
			EsMetaData md = fieldType.getField()
					.getAnnotation(EsMetaData.class);
			return new EsMetaDataInfo(md);
		}
		return null;
	}
}
