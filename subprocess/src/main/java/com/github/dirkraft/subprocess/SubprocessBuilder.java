package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubprocessBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(SubprocessBuilder.class);

	private final ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();

	private SubprocessOutput stdout = DummySubprocessOutput.INSTANCE;
	private SubprocessOutput stderr = DummySubprocessOutput.INSTANCE;
	private Runnable startCheck = () -> No.check(() -> Thread.sleep(100));
	private long patienceMs = 1000;

	public SubprocessBuilder command(String... command) {
		this.processBuilder.command(command);
		LOG.trace("command: {}", (Object) command);
		return this;
	}

	public SubprocessBuilder stdoutToTmpfile() {
		TempFileSubprocessOutput stdout = TempFileSubprocessOutput.create(".out");
		this.processBuilder.redirectOutput(stdout.getTempFile());
		this.stdout = stdout;
		LOG.trace("stdout: {}", stdout);
		return this;
	}

	public SubprocessBuilder stderrToTmpfile() {
		TempFileSubprocessOutput stderr = TempFileSubprocessOutput.create(".err");
		this.processBuilder.redirectError(stderr.getTempFile());
		this.stderr = stderr;
		LOG.trace("stderr: {}", stderr);
		return this;
	}

	public SubprocessBuilder startCheck(Runnable startCheck) {
		this.startCheck = startCheck;
		LOG.trace("startCheck: {}", startCheck);
		return this;
	}

	public SubprocessBuilder patienceMs(long patienceMs) {
		this.patienceMs = patienceMs;
		return this;
	}

	public Subprocess start() {
		return No.check(() -> {
			Process process = processBuilder.start();
			startCheck.run();
			LOG.debug("Underlying Process started: {}", process);
			return new Subprocess(process, stdout, stderr, patienceMs);
		});
	}

}
