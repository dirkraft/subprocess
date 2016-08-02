package com.github.dirkraft.subprocess;

import java.io.File;

class SubprocessStarter<C extends SubprocessController> {

	private final SubprocessBuilder<C> builder;

	private C controller;

	SubprocessStarter(SubprocessBuilder<C> builder) {
		this.builder = builder;
	}

	Subprocess2 start() {

		Process process = startRawProcess();
		controller = builder.controllerDecorator().apply(process);
		waitForStartup();

		return makeSubprocess();
	}

	private Process startRawProcess() {
		return No.check(() -> {
			ProcessBuilder pb = new ProcessBuilder()
				.command(builder.command())
				.directory(new File(builder.workingDir()));
			pb.environment().putAll(builder.environment());
			return pb.start();
		});
	}

	private void waitForStartup() {
		long expiration = System.nanoTime() + builder.startTimeoutMs() * 1000000;
		while (!builder.startCondition().check(controller)) {
			if (System.nanoTime() >= expiration) {
				throwStartupTimeExpiredException();
			}
			No.check(() -> Thread.sleep(100));
		}
	}

	private void throwStartupTimeExpiredException() {
		String message = String.format("Start condition (%s) was not satisfied in time (%dms).",
			builder.startCondition(), builder.startTimeoutMs());
		throw new SubprocessExecutionException(message);
	}

	private Subprocess2 makeSubprocess() {
		Subprocess2 subprocess = new Subprocess2();
		subprocess.controller = controller;
		subprocess.stdinFuture = new FailedFuture<>(new SubprocessException(new UnsupportedOperationException()));
		subprocess.stdoutFuture = new FailedFuture<>(new SubprocessException(new UnsupportedOperationException()));
		subprocess.stderrFuture = new FailedFuture<>(new SubprocessException(new UnsupportedOperationException()));
		return subprocess;
	}

}
