package com.github.dirkraft.subprocess.testapi;

import com.github.dirkraft.subprocess.Subprocess;
import com.github.dirkraft.subprocess.SubprocessBuilder;
import com.github.dirkraft.subprocess.SubprocessResult;
import com.github.dirkraft.subprocess.TestConst;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubprocessTest {

	@Test
	public void testGetOutput() {
		SubprocessResult result = Subprocess.getOutput(TestConst.OUTPUT_ON_SIGTERM_SCRIPT);
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
	}

	@Test
	public void testExplicitThenFinish() {
		SubprocessBuilder sh = new SubprocessBuilder()
			.command(TestConst.OUTPUT_ON_SIGTERM_SCRIPT)
			.stdoutToTmpfile()
			.stderrToTmpfile();
		SubprocessResult result = sh.start().finish();
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
	}

	@Test
	public void testDefaults() {
		SubprocessBuilder sh = new SubprocessBuilder()
			.command(TestConst.OUTPUT_ON_SIGTERM_SCRIPT);
		SubprocessResult result = sh.start().finish();
		assertEquals("", result.getStdout());
		assertEquals("", result.getStderr());
	}

	@Test
	public void testImpatient() {
		SubprocessResult result = new SubprocessBuilder()
			.command(TestConst.SLEEPY_SCRIPT)
			.stdoutToTmpfile()
			.stderrToTmpfile()
			.startCheck(() -> Thread.sleep(500))
			.finishPatienceMs(1)
			.start()
			.finish();

		assertEquals("Oh hi. Didn't see you there.", result.getStdout().trim());
		assertEquals("The patience was too low. No output captured.",
			"", result.getStderr().trim());
	}

	@Test
	public void testPatient() {
		SubprocessResult result = new SubprocessBuilder()
			.command(TestConst.SLEEPY_SCRIPT)
			.stdoutToTmpfile()
			.stderrToTmpfile()
			.startCheck(() -> Thread.sleep(500))
			.finishPatienceMs(1000)
			.start()
			.finish();

		assertEquals("Oh hi. Didn't see you there.", result.getStdout().trim());
		assertEquals("The patience was high enough. Output captured.",
			"Yawwwn", result.getStderr().trim());
	}
}
