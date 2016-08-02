package com.github.dirkraft.subprocess;

import java.util.function.Function;

// TODO C should probably be PLATFORM extends SubprocessPlatform
public class SubprocessBuilder<C extends SubprocessController> {



	private String[] command = {};
	private Function<Process, C> controllerDecorator;
	private long startTimeoutMs = 0;
	private SubprocessCondition<C> startCondition = controller -> true;

	public String[] command() {
		return this.command;
	}

	public SubprocessBuilder<C> command(String[] command) {
		this.command = command;
		return this;
	}

	public Function<Process, C> controllerDecorator() {
		return this.controllerDecorator;
	}

	public SubprocessBuilder<C> controllerDecorator(Function<Process, C> controllerDecorator) {
		this.controllerDecorator = controllerDecorator;
		return this;
	}

	public long startTimeoutMs() {
		return this.startTimeoutMs;
	}

	public SubprocessCondition<C> startCondition() {
		return this.startCondition;
	}

	public SubprocessBuilder startCondition(long timeoutMs, SubprocessCondition<C> condition) {
		this.startTimeoutMs = timeoutMs;
		this.startCondition = condition;
		return this;
	}

	public Subprocess start() {
		validateState();
		SubprocessStarter<C> starter = new SubprocessStarter<>(this);
		return starter.start();
	}

	private void validateState() {
		requireState(command.length > 0, "`command` is required");
		requireState(controllerDecorator != null, "`controllerDecorator` is required");
		requireState(startTimeoutMs >= 0, "`startTimeoutMs` must be non-negative");
		requireState(startCondition != null, "`startCondition` is required");
	}

	private void requireState(boolean condition, String descriptionFormat, Object... formatArgs) {
		if (!condition) {
			String message = String.format(descriptionFormat, formatArgs);
			throw new InvalidOperationException(message);
		}
	}

}
