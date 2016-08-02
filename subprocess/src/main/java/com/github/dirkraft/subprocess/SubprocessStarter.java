package com.github.dirkraft.subprocess;

class SubprocessStarter<C extends SubprocessController> {

	private final SubprocessBuilder<C> builder;

	private C controller;

	SubprocessStarter(SubprocessBuilder<C> builder) {
		this.builder = builder;
	}

	Subprocess start() {

		Process process = startRawProcess();
		controller = builder.controllerDecorator().apply(process);
		waitForStartup();

		return makeSubprocess(controller);
	}

	private Process startRawProcess() {
		return No.check(() -> new ProcessBuilder()
			.command(builder.command())
			.start());
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

	private Subprocess makeSubprocess(C controller) {
		Subprocess subprocess = new Subprocess();
		subprocess.controller = controller;
		subprocess.stdinFuture = new FailedFuture<>(new SubprocessException(new UnsupportedOperationException()));
		subprocess.stdoutFuture = new FailedFuture<>(new SubprocessException(new UnsupportedOperationException()));
		subprocess.stderrFuture = new FailedFuture<>(new SubprocessException(new UnsupportedOperationException()));
		return subprocess;
	}

}
