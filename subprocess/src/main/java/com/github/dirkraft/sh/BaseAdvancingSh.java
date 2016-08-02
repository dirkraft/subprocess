package com.github.dirkraft.sh;

abstract class BaseAdvancingSh implements Sh {

	private Sh callerPointer;

	protected BaseAdvancingSh() {
		callerPointer = this;
	}

	@Override
	public final ShResult getResult() {
		if (callerPointer == this) {
			callerPointer = safeAdvanceRunState();
		}
		return callerPointer.getResult();
	}

	private Sh safeAdvanceRunState() throws ShException {
		if (callerPointer != this) {
			throw new ShException("Invalid usage. Run state has already been advanced here: " + this);
		}
		return advanceRunState();
	}

	protected abstract Sh advanceRunState();
}
