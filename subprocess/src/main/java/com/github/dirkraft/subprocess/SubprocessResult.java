package com.github.dirkraft.subprocess;

public class SubprocessResult {

	private String stdout;
	private String stderr;

	SubprocessResult(String stdout, String stderr) {
		this.stdout = stdout;
		this.stderr = stderr;
	}

	public String getStdout() {
		return stdout;
	}

	public String getStderr() {
		return stderr;
	}

	public void printToSystem() {
		System.out.print(stdout);
		System.err.print(stderr);
	}

	@Override
	public String toString() {
		return "ShResult{" +
			"stdout='" + stdout + '\'' +
			", stderr='" + stderr + '\'' +
			'}';
	}
}
