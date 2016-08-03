package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

class ProcessDestroyer {

	private static final Logger LOG = LoggerFactory.getLogger(ProcessDestroyer.class);

	private final Process process;
	private final ShutdownSignaler signaler;
	private final long finishPatienceMs;

	ProcessDestroyer(Process process, ShutdownSignaler signaler, long finishPatienceMs) {
		this.process = process;
		this.signaler = signaler;
		this.finishPatienceMs = finishPatienceMs;
	}

	SubprocessExit stopProcess() {
		if (process.isAlive()) {
			LOG.trace("Shutting down underlying process: {}", process);
			boolean exited = tryShutdown();
			if (exited) {
				return SubprocessExit.GRACEFUL;

			} else {
				LOG.warn("Process did not exit. Destroying forcibly: {}", process);
				tryDestroyForcibly();
				return SubprocessExit.FORCED;
			}
		} else {
			return SubprocessExit.NORMAL;
		}
	}

	private boolean tryShutdown() {
		try {
			signaler.shutdown(process);
			return process.waitFor(finishPatienceMs, TimeUnit.MILLISECONDS);

		} catch (InterruptedException e) {
			throw new SubprocessException(e);
		}
	}

	private void tryDestroyForcibly() {
		try {
			signaler.kill(process);
			boolean exited = process.waitFor(finishPatienceMs, TimeUnit.MILLISECONDS);
			if (!exited) {
				throw new SubprocessException("Failed to close out process in a timely manner. " +
					"Caution: This may constitute a resource leak.");
			}
		} catch (InterruptedException e) {
			throw new SubprocessException(e);
		}
	}


}
