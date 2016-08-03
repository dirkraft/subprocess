package com.github.dirkraft.subprocess.testapi;

import com.github.dirkraft.subprocess.PosixAwareShutdownSignaler;
import com.github.dirkraft.subprocess.SubprocessBuilder;
import com.github.dirkraft.subprocess.SubprocessExit;
import com.github.dirkraft.subprocess.SubprocessResult;
import com.github.dirkraft.subprocess.Subprocesses;
import com.github.dirkraft.subprocess.TestConst;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubprocessTest {

	@Test
	public void testGetOutput() {
		SubprocessResult result = Subprocesses.getOutput(TestConst.OUTPUT_ON_SIGTERM_SCRIPT);
		assertTrue(result.getStdout().contains("Hello"));
		assertTrue(result.getStderr().contains("SIGTERM"));
		assertEquals(SubprocessExit.GRACEFUL, result.getExit());
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
		assertEquals(SubprocessExit.GRACEFUL, result.getExit());
	}

	@Test
	public void testDefaults() {
		SubprocessBuilder sh = new SubprocessBuilder()
			.command(TestConst.OUTPUT_ON_SIGTERM_SCRIPT);
		SubprocessResult result = sh.start().finish();
		assertEquals("", result.getStdout());
		assertEquals("", result.getStderr());
		assertEquals(SubprocessExit.GRACEFUL, result.getExit());
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
		assertEquals("It was forcibly killed because we ran out of patience.",
			SubprocessExit.FORCED, result.getExit());
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
		assertEquals(SubprocessExit.GRACEFUL, result.getExit());
	}

	@Test
	public void testSlow() {
		SubprocessResult result = Subprocesses.getOutput("bash", "-c", "sleep 0.5 ; echo Done");
		assertEquals("Done", result.getStdout().trim());
		assertEquals(SubprocessExit.NORMAL, result.getExit());
	}

	@Test
	public void testSigtermIgnored() {
		SubprocessResult result = Subprocesses.getOutput(TestConst.SIGHUP_SCRIPT);
		assertEquals(SubprocessExit.FORCED, result.getExit());
	}

	@Test
	public void testCustomShutdownSignal() {
		SubprocessResult result = Subprocesses.forOutput(TestConst.SIGHUP_SCRIPT)
			.shutdownSignaler(new PosixAwareShutdownSignaler("kill", "-SIGHUP"))
			.start()
			.finish();
		System.out.println("erp");
		result.printToSystem();
		System.out.println("derp");
		assertEquals(SubprocessExit.GRACEFUL, result.getExit());
	}

}
