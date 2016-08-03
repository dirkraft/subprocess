package com.github.dirkraft.subprocess;

class Except {

	static void unchecked(ExceptingRunnable runnable) {
		try {
			runnable.run();
		} catch (SubprocessException e) {
			throw e;
		} catch (Exception e) {
			throw new SubprocessException(e);
		}
	}

	static <T> T unchecked(ExceptingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (SubprocessException e) {
			throw e;
		} catch (Exception e) {
			throw new SubprocessException(e);
		}
	}

	static <T> T log(ExceptingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SubprocessException(e);
		}
	}

	static <T> T suppress(ExceptingSupplier<T> supplier, T defaultValue) {
		try {
			return supplier.get();
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
