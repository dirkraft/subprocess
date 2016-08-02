package com.github.dirkraft.subprocess.testapi;

import com.github.dirkraft.subprocess.Subprocess;
import com.github.dirkraft.subprocess.SubprocessResult;
import com.github.dirkraft.subprocess.TestConst;
import com.github.dirkraft.subprocess.SubprocessBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubprocessTest {

	@Test
	public void testEval() {
		SubprocessResult result = Subprocess.eval(TestConst.TRAP_SCRIPT);
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
	}

	@Test
	public void testStartThenFinish() {
		Subprocess subprocess = Subprocess.start(TestConst.TRAP_SCRIPT);
		SubprocessResult result = subprocess.finish();
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
	}

	@Test
	public void testExplicitThenFinish() {
		SubprocessBuilder sh = new SubprocessBuilder()
			.command(TestConst.TRAP_SCRIPT)
			.stdoutToTmpfile()
			.stderrToTmpfile();
		SubprocessResult result = sh.start().finish();
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
	}

	@Test
	public void testDefaults() {
		SubprocessBuilder sh = new SubprocessBuilder().command(TestConst.TRAP_SCRIPT);
		SubprocessResult result = sh.start().finish();
		assertEquals("", result.getStdout());
		assertEquals("", result.getStderr());
	}
}
