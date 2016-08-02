package com.github.dirkraft.sh;

class DummyShOutStream implements ShOutStream {

	static final DummyShOutStream INSTANCE = new DummyShOutStream();

	@Override
	public String getUnread() {
		return "";
	}

	@Override
	public void close() throws ShException {
	}
}
