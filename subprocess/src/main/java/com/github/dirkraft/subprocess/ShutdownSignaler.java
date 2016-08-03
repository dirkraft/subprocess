package com.github.dirkraft.subprocess;

public interface ShutdownSignaler {

	void shutdown(Process p);

	void kill(Process p);

}
