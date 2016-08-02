package com.github.dirkraft.subprocess;

public interface SubprocessCondition<C extends SubprocessController> {

	boolean check(C controller);
}
