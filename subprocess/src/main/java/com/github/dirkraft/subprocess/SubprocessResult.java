package com.github.dirkraft.subprocess;

public class SubprocessResult {

	private int exitCode;
	private String stdout;
	private String stderr;

	SubprocessResult(int exitCode, String stdout, String stderr) {
		this.exitCode = exitCode;
		this.stdout = stdout;
		this.stderr = stderr;
	}

	public int getExitCode() {
		return exitCode;
	}

	public String getStdout() {
		return stdout;
	}

	public String getStderr() {
		return stderr;
	}
}
