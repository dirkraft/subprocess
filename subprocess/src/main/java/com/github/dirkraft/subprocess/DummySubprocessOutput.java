package com.github.dirkraft.subprocess;

public class DummySubprocessOutput implements SubprocessOutput {

	public static final DummySubprocessOutput INSTANCE = new DummySubprocessOutput();

	private DummySubprocessOutput() {
	}

	@Override
	public String getUnread() {
		return "";
	}

	@Override
	public void close() throws SubprocessException {
	}
}
