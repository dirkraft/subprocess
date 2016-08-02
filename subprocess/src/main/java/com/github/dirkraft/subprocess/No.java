package com.github.dirkraft.subprocess;

class No {

	static void check(ExceptingRunnable runnable) {
		try {
			runnable.run();
		} catch (SubprocessException e) {
			throw e;
		} catch (Exception e) {
			throw new SubprocessException(e);
		}
	}

	static <T> T check(ExceptingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (SubprocessException e) {
			throw e;
		} catch (Exception e) {
			throw new SubprocessException(e);
		}
	}

	@FunctionalInterface
	interface ExceptingRunnable {
		void run() throws Exception;
	}

	@FunctionalInterface
	interface ExceptingSupplier<T> {
		T get() throws Exception;
	}
}
