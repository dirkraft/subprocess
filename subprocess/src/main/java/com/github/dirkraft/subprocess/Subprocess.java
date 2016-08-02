package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Subprocess implements AutoCloseable {

	public static SubprocessResult eval(String commandName, String... commandArgs) {
		return start(commandName, commandArgs).finish();
	}

	public static Subprocess start(String commandName, String... commandArgs) {
		return new SubprocessBuilder()
			.command(Arrays.concat(commandName, commandArgs))
			.stdoutToTmpfile()
			.stderrToTmpfile()
			.start();
	}

	private static final Logger LOG = LoggerFactory.getLogger(Subprocess.class);

	private final Process process;
	private final SubprocessOutput stdout, stderr;
	private final long patienceMs;

	public Subprocess(Process process, SubprocessOutput stdout, SubprocessOutput stderr, long patienceMs) {
		this.process = process;
		this.stdout = stdout;
		this.stderr = stderr;
		this.patienceMs = patienceMs;
	}

	public SubprocessResult finish() {
		return shutdown();
	}

	private SubprocessResult shutdown() {
		closeProcess();
		SubprocessResult result = new SubprocessResult(stdout.getUnread(), stderr.getUnread());
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
			return process.waitFor(patienceMs, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new SubprocessException(e);
		}
	}

	private void tryDestroyForcibly() {
		try {
			boolean exited = process.destroyForcibly()
				.waitFor(patienceMs, TimeUnit.MILLISECONDS);
			if (!exited) {
				throw new SubprocessException("Failed to close out process in a timely manner. " +
					"Caution: This may constitute a resource leak.");
			}
		} catch (InterruptedException e) {
			throw new SubprocessException(e);
		}
	}

	@Override
	public void close() throws Exception {
		finish();
	}

}
