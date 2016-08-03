package com.github.dirkraft.subprocess;

/**
 * The default signaler simply relays 'shutdown' to {@link Process#destroy()} and 'kill' to
 * {@link Process#destroyForcibly()}
 */
public class DefaultShutdownSignaler implements ShutdownSignaler {

	public static final DefaultShutdownSignaler INSTANCE = new DefaultShutdownSignaler();

	@Override
	public void shutdown(Process p) {
		p.destroy();
	}

	@Override
	public void kill(Process p) {
		p.destroyForcibly();
	}
}
