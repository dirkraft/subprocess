package com.github.dirkraft.subprocess;

class OsxSubprocessController implements SubprocessController {

	private final Process process;

	OsxSubprocessController(Process process) {
		this.process = process;
	}

	@Override
	public int shutdown() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void terminate() {
		throw new UnsupportedOperationException();
	}
}
