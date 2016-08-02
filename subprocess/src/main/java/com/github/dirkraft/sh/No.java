package com.github.dirkraft.sh;

import java.util.ArrayList;
import java.util.List;

class No {

	static void check(ExceptingRunnable runnable) {
		try {
			runnable.run();
		} catch (ShException e) {
			throw e;
		} catch (Exception e) {
			throw new ShException(e);
		}
	}

	static <T> T check(ExceptingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (ShException e) {
			throw e;
		} catch (Exception e) {
			throw new ShException(e);
		}
	}

	static <T> T except(ExceptingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
