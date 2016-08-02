package com.github.dirkraft.sh;

public class ShException extends RuntimeException {

	public ShException() {
	}

	public ShException(String message) {
		super(message);
	}

	public ShException(Throwable cause) {
		super(cause);
	}

	public ShException(String message, Throwable cause) {
		super(message, cause);
	}
}
