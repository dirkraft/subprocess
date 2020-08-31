package com.github.dirkraft.subprocess;

public class SubprocessResult {

	private Subprocess subprocess;
	private String stdout;
	private String stderr;
	private SubprocessExit exit;

	SubprocessResult(Subprocess subprocess, String stdout, String stderr, SubprocessExit exit) {
		this.subprocess = subprocess;
		this.stdout = stdout;
		this.stderr = stderr;
		this.exit = exit;
	}

	public Subprocess getSubprocess() {
		return subprocess;
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
