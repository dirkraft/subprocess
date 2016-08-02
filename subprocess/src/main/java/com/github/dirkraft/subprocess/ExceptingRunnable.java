package com.github.dirkraft.subprocess;

@FunctionalInterface
public interface ExceptingRunnable {
	void run() throws Exception;
}
