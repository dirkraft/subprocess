package com.github.dirkraft.sh;

public interface Sh {

	static ShResult eval(String commandName, String... commandArgs) {
		return start(commandName, commandArgs).getResult();
	}

	static RunningSh start(String commandName, String... commandArgs) {
		return new UnstartedSh()
			.command(ShArrays.concat(commandName, commandArgs))
			.stdoutToTmpfile()
			.stderrToTmpfile()
			.advanceRunState();
	}

	ShResult getResult();
}
