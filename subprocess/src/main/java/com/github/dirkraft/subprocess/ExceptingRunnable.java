package com.github.dirkraft.subprocess;

@FunctionalInterface
interface ExceptingRunnable {
	void run() throws Exception;
}
