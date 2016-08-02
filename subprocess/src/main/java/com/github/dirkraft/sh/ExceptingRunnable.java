package com.github.dirkraft.sh;

@FunctionalInterface
interface ExceptingRunnable {
	void run() throws Exception;
}
