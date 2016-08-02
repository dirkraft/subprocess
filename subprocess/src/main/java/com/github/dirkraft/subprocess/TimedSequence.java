package com.github.dirkraft.subprocess;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class TimedSequence {

	private static final Supplier<Long> SYSTEM_TIME_SUPPLIER = () -> System.nanoTime() / 1000000;

	public static TimedSequence fromNow(long deadlineMs) {
		long deadline = SYSTEM_TIME_SUPPLIER.get() + deadlineMs;
		return new TimedSequence(SYSTEM_TIME_SUPPLIER, deadline);
	}

	private final long unixDeadlineMs;
	private final Supplier<Long> timeSupplier;

	private final Queue<ExceptingRunnable> tasks = new LinkedList<>();

	public TimedSequence(Supplier<Long> timeSupplier, long unixDeadlineMs) {
		this.unixDeadlineMs = unixDeadlineMs;
		this.timeSupplier = timeSupplier;
	}

	public TimedSequence then(ExceptingRunnable runnable) {
		tasks.add(runnable);
		return this;
	}

	public TimedSequence run() {
		while(hasTimeRemaining() && !tasks.isEmpty()) {
			ExceptingRunnable task = tasks.remove();
			No.check(task);
		}
		return this;
	}

	private boolean hasTimeRemaining() {
		return timeSupplier.get() < unixDeadlineMs;
	}

	public boolean completed() {
		return tasks.isEmpty();
	}
}
