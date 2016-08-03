package com.github.dirkraft.subprocess;

import java.util.concurrent.TimeUnit;

public class Subprocess implements AutoCloseable {

	private final Process process;
	private final SubprocessOutput stdout, stderr;
	private final ProcessDestroyer processDestroyer;
	private final long finishPatienceMs;

	public Subprocess(Process process, SubprocessOutput stdout, SubprocessOutput stderr, ProcessDestroyer processDestroyer, long finishPatienceMs) {
		this.process = process;
		this.stdout = stdout;
		this.stderr = stderr;
		this.processDestroyer = processDestroyer;
		this.finishPatienceMs = finishPatienceMs;
	}

	@Override
	public void close() throws SubprocessException {
		finish();
	}

	public SubprocessResult finish() {
		return finish(finishPatienceMs);
	}

	public SubprocessResult finish(long timeoutMs) {
		Except.unchecked(() -> process.waitFor(timeoutMs, TimeUnit.MILLISECONDS));
		return finishImmediately();
	}

	public SubprocessResult finishImmediately() {
		return shutdown();
	}

	private SubprocessResult shutdown() {
		SubprocessExit exit = processDestroyer.stopProcess();
		SubprocessResult result = new SubprocessResult(stdout.getUnread(), stderr.getUnread(), exit);
		stdout.close();
		stderr.close();
		return result;
	}

}
