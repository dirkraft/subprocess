package com.github.dirkraft.subprocess;

public enum SubprocessExit {

	/**
	 * The process exited normally on its own without any signaling from us.
	 */
	NORMAL,

	/**
	 * The process exited gracefully in response to a user-initiated shutdown via the configured
	 * {@link ShutdownSignaler#shutdown(Process)}.
	 */
	GRACEFUL,

	/**
	 * The process failed to exit gracefully and was forcefully terminated in response to a user-initiated
	 * shutdown via the configured {@link ShutdownSignaler#kill(Process)}.
	 */
	FORCED
}
