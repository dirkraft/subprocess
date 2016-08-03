package com.github.dirkraft.subprocess;

/**
 * A dummy implementation which captures none of the output.
 */
public class DummySubprocessOutput implements SubprocessOutput {

	public static final DummySubprocessOutput INSTANCE = new DummySubprocessOutput();

	private DummySubprocessOutput() {
	}

	@Override
	public ProcessBuilder.Redirect asRedirect() {
		return ProcessBuilder.Redirect.INHERIT;
	}

	@Override
	public String getUnread() {
		return "";
	}

	@Override
	public void close() throws SubprocessException {
	}
}
