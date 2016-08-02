package com.github.dirkraft.subprocess;

public interface SubprocessOutput extends AutoCloseable {

	ProcessBuilder.Redirect asRedirect();

	String getUnread();

	void close() throws SubprocessException;

}
