package com.github.dirkraft.sh.testapi;

import com.github.dirkraft.sh.Sh;
import com.github.dirkraft.sh.ShResult;
import com.github.dirkraft.sh.TestConst;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ShTest {

	@Test
	public void testEval() {
		ShResult result = Sh.eval(TestConst.TRAP_SCRIPT);
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
	}

}
