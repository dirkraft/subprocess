package com.github.dirkraft.subprocess;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Subprocess implements AutoCloseable {

	private static final String TMP_PFX = "subprocess";
	private final Process process;

	public static Subprocess exec(String commandName, String... commandArgs) {

		String[] fullCommand = new String[commandArgs.length + 1];
		fullCommand[0] = commandName;
		System.arraycopy(commandArgs, 0, fullCommand, 1, commandArgs.length);

		File stdout = No.check(() -> File.createTempFile(TMP_PFX, ".out"));
		File stderr = No.check(() -> File.createTempFile(TMP_PFX, ".err"));
		Process process = No.check(() -> new ProcessBuilder()
			.command(fullCommand)
			.redirectOutput(stdout)
			.redirectError(stderr)
			.start());

		return new Subprocess(process);
	}

	protected Subprocess(Process process) {
		this.process = process;
	}

	@Override
	public void close() throws Exception {
		if (process.isAlive()) {
			boolean exited = process.destroyForcibly()
				.waitFor(5, TimeUnit.SECONDS);
			if (!exited) {
				throw new SubprocessException(
					"Failed to close out process in a timely manner. Caution: This may constitute a resource leak.");
			}
		}
	}
}
