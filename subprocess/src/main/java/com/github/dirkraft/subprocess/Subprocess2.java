package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Subprocess2<C extends SubprocessController> implements AutoCloseable {

	private static final Logger LOG = LoggerFactory.getLogger(Subprocess2.class);

	C controller;
	Future<String> stdinFuture, stdoutFuture, stderrFuture;

	private SubprocessResult result;

	public static Subprocess2 exec(String... command) {
		return PlatformDefaults.get()
			.command(command)
			.start();
	}

	public Subprocess2 waitFor(long timeoutMs) {
		throw new UnsupportedOperationException();
	}

	public Subprocess2 waitFor(long timeoutMs, SubprocessCondition<C> condition) {
		throw new UnsupportedOperationException();
	}

	public boolean isAlive() {
		throw new UnsupportedOperationException();
	}

	public Subprocess2 shutdown(long timeoutMs) {
		if (result != null) {
			throw new InvalidOperationException("This subprocess has already been shutdown.");
		}

		// TODO condition

		int exitCode = controller.shutdown();
		// TODO timeoutMs is the total. Subtract remaining time for each potential wait.
		String stdout = No.check(() -> stdoutFuture.get(timeoutMs, TimeUnit.MILLISECONDS));
		String stderr = No.check(() -> stderrFuture.get(timeoutMs, TimeUnit.MILLISECONDS));

		this.result = new SubprocessResult(exitCode, stdout, stderr);

		return this;
	}

	@Override
	public void close() throws SubprocessException {
		if (result != null) {
//			shutdown();
		}

		// TODO forceful cleanup
		throw new UnsupportedOperationException();
	}

	public SubprocessResult getResult() {
		if (result == null) {
			throw new InvalidOperationException(
				"Result is not available until this Subprocess has been shutdown.");
		}
		return result;
	}

	public SubprocessResult then(String... nextCommand) {
		// TODO shutdown
		// TODO close
		throw new UnsupportedOperationException();
	}

	public SubprocessResult then(Function<SubprocessResult, String[]> nextCommand) {
		// TODO shutdown
		// TODO close
		throw new UnsupportedOperationException();
	}
}
