package com.github.dirkraft.subprocess.test;

import com.github.dirkraft.subprocess.Subprocess;
import com.github.dirkraft.subprocess.SubprocessResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubprocessTest {

	@Test
	public void testImmediatelyExitingCommand() {
		SubprocessResult result = Subprocess.exec("ls", "-al", "gradle/").shutdown().getResult();

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
