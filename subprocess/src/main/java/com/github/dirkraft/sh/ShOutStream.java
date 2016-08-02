package com.github.dirkraft.sh;

public interface ShOutStream extends AutoCloseable {

	String getUnread();

	void close() throws ShException;
}
