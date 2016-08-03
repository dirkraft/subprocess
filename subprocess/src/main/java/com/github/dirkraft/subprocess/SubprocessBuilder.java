package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fluent builder for {@link Subprocess}. Start the subprocess via {@link #start()}. Defaults are
 * <ul>
 * <li>stdout &amp; stderr: {@link DummySubprocessOutput} which results in output behaving like
 * {@link ProcessBuilder#inheritIO()}</li>
 * <li>startCheck: no-op which returns immediately, i.e. the subprocess is returned immdiately on {@link #start()}</li>
 * <li>shutdownSignaler: {@link DefaultShutdownSignaler}</li>
 * <li>finishPatienceMs: {@value #DEFAULT_FINISH_PATIENCE_MS}</li>
 * </ul>
 */
public class SubprocessBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(SubprocessBuilder.class);

	private static final long DEFAULT_FINISH_PATIENCE_MS = 1000;

	private final ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();

	private SubprocessOutput stdout = DummySubprocessOutput.INSTANCE;
	private SubprocessOutput stderr = DummySubprocessOutput.INSTANCE;
	private ExceptingRunnable startCheck = () -> {
	};
	private ShutdownSignaler shutdownSignaler = DefaultShutdownSignaler.INSTANCE;
	private long finishPatienceMs = DEFAULT_FINISH_PATIENCE_MS;

	/**
	 * @param command Pass each command argument as a separate string,
	 *                e.g. "ls -al" would be passed as "ls", "-al"
	 * @return this, for chaining
	 */
	public SubprocessBuilder command(String... command) {
		this.processBuilder.command(command);
		LOG.trace("command: {}", (Object) command);
		return this;
	}

	/**
	 * Redirect stdout to a temp file which we can read later to retrieve all output.
	 *
	 * @return this, for chaining
	 */
	public SubprocessBuilder stdoutToTmpfile() {
		return stdout(TempFileSubprocessOutput.create(".out"));
	}

	/**
	 * @param stdout output handler
	 * @return this, for chaining
	 */
	public SubprocessBuilder stdout(SubprocessOutput stdout) {
		ProcessBuilder.Redirect redirect = stdout.asRedirect();
		this.processBuilder.redirectOutput(redirect);
		this.stdout = stdout;
		LOG.trace("stdout: {}", stdout);
		return this;
	}

	/**
	 * Redirect stderr to a temp file which we can read later to retrieve all output.
	 */
	public SubprocessBuilder stderrToTmpfile() {
		return stderr(TempFileSubprocessOutput.create(".err"));
	}

	/**
	 * @param stderr output handler
	 * @return this, for chaining
	 */
	public SubprocessBuilder stderr(SubprocessOutput stderr) {
		ProcessBuilder.Redirect redirect = stderr.asRedirect();
		this.processBuilder.redirectError(redirect);
		this.stderr = stderr;
		LOG.trace("stderr: {}", stderr);
		return this;
	}

	/**
	 * @param startCheck Arbitrary logic which blocks until the process can be considered in a "started" state.
	 * @return this, for chaining
	 */
	public SubprocessBuilder startCheck(ExceptingRunnable startCheck) {
		this.startCheck = startCheck;
		LOG.trace("startCheck: {}", startCheck);
		return this;
	}

	/**
	 * @param shutdownSignaler used to shutdown and terminate the underlying {@link Process}.
	 *                         Different subprocesses are terminated (gracefully) in different ways.
	 * @return this, for chaining
	 */
	public SubprocessBuilder shutdownSignaler(ShutdownSignaler shutdownSignaler) {
		this.shutdownSignaler = shutdownSignaler;
		return this;
	}

	/**
	 * @param timeoutMs the patience intervals used in shutting down the process. This interval may
	 *                  be applied several times during shutdown.
	 * @return this, for chaining
	 */
	public SubprocessBuilder finishPatienceMs(long timeoutMs) {
		this.finishPatienceMs = timeoutMs;
		return this;
	}

	public Subprocess start() {
		return Except.unchecked(() -> {
			Process process = processBuilder.start();
			startCheck.run();
			LOG.debug("Underlying Process started: {}", process);
			return new Subprocess(process,
				stdout,
				stderr,
				new ProcessDestroyer(process, shutdownSignaler, finishPatienceMs),
				finishPatienceMs);
		});
	}

}
