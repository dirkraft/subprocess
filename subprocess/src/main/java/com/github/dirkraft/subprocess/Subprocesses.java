package com.github.dirkraft.subprocess;

public class Subprocesses {

	/**
	 * @return the result of a subprocess configured to capture all output.
	 */
	public static SubprocessResult getOutput(String commandName, String... commandArgs) {
		return forOutput(commandName, commandArgs)
			.start()
			.finish();
	}

	/**
	 * @return a new subprocess builder configured to capture all output in the result.
	 */
	public static SubprocessBuilder forOutput(String commandName, String... commandArgs) {
		return new SubprocessBuilder()
			.command(Arrays.concat(commandName, commandArgs))
			.stdoutToTmpfile()
			.stderrToTmpfile();
	}
}
