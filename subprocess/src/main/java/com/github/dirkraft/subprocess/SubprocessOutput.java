package com.github.dirkraft.subprocess;

public interface SubprocessOutput extends AutoCloseable {

	String getUnread();

	void close() throws SubprocessException;
}
