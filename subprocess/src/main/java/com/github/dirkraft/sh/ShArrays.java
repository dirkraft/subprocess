package com.github.dirkraft.sh;

class ShArrays {
	static String[] concat(String first, String[] array) {
		String[] together = new String[array.length + 1];
		together[0] = first;
		System.arraycopy(array, 0, together, 1, array.length);
		return together;
	}
}
