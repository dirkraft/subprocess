package com.github.dirkraft.sh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

class RunningSh extends BaseAdvancingSh {

	private static final Logger LOG = LoggerFactory.getLogger(RunningSh.class);

	private final Process process;
	private final ShOutStream stdout, stderr;

	RunningSh(Process process, ShOutStream stdout, ShOutStream stderr) {
		this.process = process;
		this.stdout = stdout;
		this.stderr = stderr;
	}

	@Override
	protected FinishedSh advanceRunState() {
		ShResult result = shutdown();
		return new FinishedSh(result);
	}

	private ShResult shutdown() {
		closeProcess();
		ShResult result = new ShResult(stdout.getUnread(), stderr.getUnread());
		stdout.close();
		stderr.close();
		return result;
	}

	private void closeProcess() {
		if (process.isAlive()) {
			LOG.trace("Shutting down underlying process: {}", process);
			boolean exited = tryShutdown();
			if (!exited) {
				LOG.warn("Process did not exit. Destroying forcibly: {}", process);
				tryDestroyForcibly();
			}
		}
	}

	private boolean tryShutdown() {
		try {
			process.destroy();
			return process.waitFor(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new ShException(e);
		}
	}

	private void tryDestroyForcibly() {
		try {
			boolean exited = process.destroyForcibly()
				.waitFor(5, TimeUnit.SECONDS);
			if (!exited) {
				throw new ShException("Failed to close out process in a timely manner. " +
					"Caution: This may constitute a resource leak.");
			}
		} catch (InterruptedException e) {
			throw new ShException(e);
		}
	}

}
