package com.github.dirkraft.sh;

public class FinishedSh implements Sh {

	private final ShResult result;

	public FinishedSh(ShResult result) {
		this.result = result;
	}

	@Override
	public ShResult getResult() {
		return result;
	}
}
