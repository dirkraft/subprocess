package com.github.dirkraft.subprocess;

public class SubprocessResult {

	private String stdout;
	private String stderr;
	private SubprocessExit exit;

	SubprocessResult(String stdout, String stderr, SubprocessExit exit) {
		this.stdout = stdout;
		this.stderr = stderr;
		this.exit = exit;
	}

	public String getStdout() {
		return stdout;
	}

	public String getStderr() {
		return stderr;
	}

	public SubprocessExit getExit() {
		return exit;
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
