package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubprocessBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(SubprocessBuilder.class);

	private final ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();

	private SubprocessOutput stdout = DummySubprocessOutput.INSTANCE;
	private SubprocessOutput stderr = DummySubprocessOutput.INSTANCE;
	private ExceptingRunnable startCheck = () -> Thread.sleep(100);
	private long finishPatienceMs = 1000;

	/**
	 * Set the subprocess command to run. Pass each command argument as a separate string,
	 * e.g. "ls -al" would be passed as "ls", "-al"
	 */
	public SubprocessBuilder command(String... command) {
		this.processBuilder.command(command);
		LOG.trace("command: {}", (Object) command);
		return this;
	}

	/**
	 * Redirect stdout to a temp file which we can read later to retrieve all output.
	 */
	public SubprocessBuilder stdoutToTmpfile() {
		return stdout(TempFileSubprocessOutput.create(".out"));
	}

	/**
	 * Set the stdout handler with a custom implementation.
	 */
	public SubprocessBuilder stdout(SubprocessOutput stdout) {
		ProcessBuilder.Redirect redirect = stdout.asRedirect();
		if (redirect != null) {
			this.processBuilder.redirectOutput(redirect);
		}
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
	 * Set the stderr handler with a custom implementation.
	 */
	public SubprocessBuilder stderr(SubprocessOutput stderr) {
		ProcessBuilder.Redirect redirect = stderr.asRedirect();
		if (redirect != null) {
			this.processBuilder.redirectError(redirect);
		}
		this.stderr = stderr;
		LOG.trace("stderr: {}", stderr);
		return this;
	}

	/**
	 * Arbitrary logic which blocks until the process can be considered in a "started" state.
	 */
	public SubprocessBuilder startCheck(ExceptingRunnable startCheck) {
		this.startCheck = startCheck;
		LOG.trace("startCheck: {}", startCheck);
		return this;
	}

	/**
	 * Set the patience intervals used in shutting down the process. This interval may
	 * be applied several times during shutdown.
	 */
	public SubprocessBuilder finishPatienceMs(long timeoutMs) {
		this.finishPatienceMs = timeoutMs;
		return this;
	}

	public Subprocess start() {
		return No.check(() -> {
			Process process = processBuilder.start();
			startCheck.run();
			LOG.debug("Underlying Process started: {}", process);
			return new Subprocess(process, stdout, stderr, finishPatienceMs);
		});
	}

}
