package com.daesang.rpa.common;

import java.util.LinkedHashMap;

public class CamelCaseMap extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -1837844016467298204L;

	@Override
	public Object put(String key, Object value) {

		return super.put(this.toCamelCase(key), value);
	}

	private String toCamelCase(String s) {

		String[] parts = s.split("_");
		StringBuilder camelCaseString = new StringBuilder();

		boolean flag = false;
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];

			if (i != 0) {
				flag = true;
			} else {
				flag = false;
			}

			camelCaseString.append(this.toProperCase(part, flag));
		}

		return camelCaseString.toString();
	}

	private String toProperCase(String s, boolean isCapital) {

		String rtnValue = "";

		if (isCapital) {
			rtnValue = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
		} else {
			rtnValue = s.toLowerCase();
		}

		return rtnValue;
	}
}