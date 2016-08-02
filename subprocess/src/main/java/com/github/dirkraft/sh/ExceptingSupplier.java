package com.github.dirkraft.sh;

@FunctionalInterface
interface ExceptingSupplier<T> {
	T get() throws Exception;
}
