package com.github.dirkraft.subprocess;

public class SubprocessException extends RuntimeException {

	public SubprocessException() {
	}

	public SubprocessException(String message) {
		super(message);
	}

	public SubprocessException(Throwable cause) {
		super(cause);
	}

	public SubprocessException(String message, Throwable cause) {
		super(message, cause);
	}
}
