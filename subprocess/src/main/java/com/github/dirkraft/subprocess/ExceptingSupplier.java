package com.github.dirkraft.subprocess;

@FunctionalInterface
interface ExceptingSupplier<T> {
	T get() throws Exception;
}
