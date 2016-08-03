package com.github.dirkraft.subprocess;

class Arrays {

	static String[] concat(String first, String[] array) {
		String[] together = new String[array.length + 1];
		together[0] = first;
		System.arraycopy(array, 0, together, 1, array.length);
		return together;
	}

	static String[] concat(String[] array, String... more) {
		String[] together = new String[array.length + more.length];
		System.arraycopy(array, 0, together, 0, array.length);
		System.arraycopy(more, 0, together, array.length, more.length);
		return together;
	}
}
