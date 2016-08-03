package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Shutdown will attempt to use the configured kill command instead of {@link Process#destroy()}. Otherwise
 * falls back to <code>process.destroy()</code>.
 */
public class PosixAwareShutdownSignaler implements ShutdownSignaler {

	private static final Logger LOG = LoggerFactory.getLogger(PosixAwareShutdownSignaler.class);

	private final String[] killCommand;

	public PosixAwareShutdownSignaler(String command, String... args) {
		this.killCommand = Arrays.concat(command, args);
	}

	@Override
	public void shutdown(Process p) {

		Field pidField = Except.suppress(() -> p.getClass().getDeclaredField("pid"), null);
		if (pidField != null) {
			if (!pidField.isAccessible()) {
				pidField.setAccessible(true);
			}
			String pid = Objects.toString(Except.unchecked(() -> pidField.get(p)));
			String[] totalKillCommand = Arrays.concat(killCommand, pid);
			LOG.debug("Sending specialized kill command: {}", (Object) totalKillCommand);

			boolean exited = Except.unchecked(() -> Runtime.getRuntime().exec(totalKillCommand)
				.waitFor(1, TimeUnit.SECONDS));
			if (!exited) {
				LOG.warn("Kill command took too long to exit. I don't know if it worked or is still hanging around!");
			}

		} else {
			p.destroy();
		}
	}

	@Override
	public void kill(Process p) {
		p.destroyForcibly();
	}
}
