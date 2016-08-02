package com.github.dirkraft.subprocess.test;

import com.github.dirkraft.subprocess.Subprocess2;
import com.github.dirkraft.subprocess.SubprocessResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Subprocess2Test {

	@Test
	public void testImmediatelyExitingCommand() {
		SubprocessResult result = Subprocess2.exec("ls", "-al", "gradle/")
			.shutdown(1000)
			.getResult();

		assertEquals(0, result.getExitCode());
	}

	@Test
	public void testEventuallyExitingCommand() {
		fail();
	}

	@Test
	public void testDaemonLikeCommand() {
		fail();
	}

	@Test
	public void testDaemonLikeCommandWithShutdownOutput() {
		fail();
	}

	@Test
	public void testErroringCommand() {
		fail();
	}

	@Test
	public void testErroringCommandSequence() {
		fail();
	}

	@Test
	public void testHappyCommandSequence() {
		fail();
	}
}
