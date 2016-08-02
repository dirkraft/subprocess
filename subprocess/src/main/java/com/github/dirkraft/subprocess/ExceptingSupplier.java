package com.github.dirkraft.subprocess;

@FunctionalInterface
public interface ExceptingSupplier<T> {
    T get() throws Exception;
}
