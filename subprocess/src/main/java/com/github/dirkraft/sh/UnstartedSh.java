package com.github.dirkraft.sh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnstartedSh extends BaseAdvancingSh {

	private static final Logger LOG = LoggerFactory.getLogger(UnstartedSh.class);

	private final ProcessBuilder processBuilder = new ProcessBuilder();

	private ShOutStream stdout = DummyShOutStream.INSTANCE;
	private ShOutStream stderr = DummyShOutStream.INSTANCE;

	public UnstartedSh command(String... command) {
		this.processBuilder.command(command);
		LOG.trace("command: {}", (Object) command);
		return this;
	}

	public UnstartedSh stdoutToTmpfile() {
		TempFileShOutStream stdout = TempFileShOutStream.create(".out");
		this.processBuilder.redirectOutput(stdout.getTempFile());
		this.stdout = stdout;
		LOG.trace("stdout: {}", stdout);
		return this;
	}

	public UnstartedSh stderrToTmpfile() {
		TempFileShOutStream stderr = TempFileShOutStream.create(".err");
		this.processBuilder.redirectError(stderr.getTempFile());
		this.stderr = stderr;
		LOG.trace("stderr: {}", stderr);
		return this;
	}

	@Override
	protected RunningSh advanceRunState() {
		return No.check(() -> {
			Process process = processBuilder.start();
			LOG.debug("Underlying Process started: {}", process);
			return new RunningSh(process, stdout, stderr);
		});
	}

}
