package com.github.dirkraft.subprocess.test;

import com.github.dirkraft.subprocess.TimedSequence;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimedSequenceTest {

	@Test
	public void testCompleted() {

		final Holder<Boolean> lastTaskWasPerformed = new Holder<>(false);

		boolean completed = TimedSequence.fromNow(100)
			.then(() -> Thread.sleep(1))
			.then(() -> Thread.sleep(1))
			.then(() -> Thread.sleep(1))
			.then(() -> lastTaskWasPerformed.value = true)
			.run()
			.completed();

		assertTrue("All tasks should have been able to complete within the time limit.", completed);
		assertTrue("The final task should have been performed.", lastTaskWasPerformed.value);
	}

	@Test
	public void testExpired() {

		final Holder<Boolean> lastTaskWasPerformed = new Holder<>(false);

		boolean completed = TimedSequence.fromNow(100)
			.then(() -> Thread.sleep(200))
			.then(() -> lastTaskWasPerformed.value = true)
			.run()
			.completed();

		assertFalse("The sleep should have prevented the sequence's completion.", completed);
		assertFalse("The last task should not have been performed.", lastTaskWasPerformed.value);
	}

}
