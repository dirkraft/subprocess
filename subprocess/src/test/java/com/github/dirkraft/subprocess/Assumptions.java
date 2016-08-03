package com.github.dirkraft.subprocess;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Assumptions {

	@Test
	public void testAssumptionRedirectsLoseOutputOnShutdown() throws IOException, InterruptedException {
		File outfile = File.createTempFile("ShTest", ".out");
		outfile.deleteOnExit();
		File errfile = File.createTempFile("ShTest", ".err");
		errfile.deleteOnExit();

		Process p = new ProcessBuilder(TestConst.OUTPUT_ON_SIGTERM_SCRIPT)
			.redirectOutput(outfile)
			.redirectError(errfile)
			.start();

		assertFalse("This process should be persistent.", p.waitFor(1, TimeUnit.SECONDS));
		p.destroy();
		assertTrue("Process should have exited", p.waitFor(1, TimeUnit.SECONDS));

		String stdout = FileUtils.readFileToString(outfile, UTF_8);
		assertTrue(stdout.contains("Hello"));

		String stderr = FileUtils.readFileToString(errfile, UTF_8);
		assertTrue(stderr.contains("SIGTERM"));
	}

	@Test
	public void testAssumptionReaderThreadsLoseOutputOnShutdown() throws IOException, InterruptedException {
		Process p = new ProcessBuilder(TestConst.OUTPUT_ON_SIGTERM_SCRIPT)
			.start();

		ExecutorService executorService = Executors.newCachedThreadPool();

		final Holder<String> stdoutHolder = new Holder<>();
		executorService.submit(() -> {
			stdoutHolder.value = Except.log(() -> IOUtils.toString(p.getInputStream(), UTF_8));
		});

		final Holder<String> stderrHolder = new Holder<>();
		executorService.submit(() -> {
			stderrHolder.value = Except.log(() -> IOUtils.toString(p.getErrorStream(), UTF_8));
		});

		assertFalse("This process should be persistent.", p.waitFor(1, TimeUnit.SECONDS));
		p.destroy();
		assertTrue("Process should have exited", p.waitFor(1, TimeUnit.SECONDS));

		executorService.shutdown();
		assertTrue("Stream threads executor stopped", executorService.awaitTermination(1, TimeUnit.SECONDS));

		assertTrue("This output happened before p.destroy(), so it comes through fine.",
			stdoutHolder.value.contains("Hello"));
		assertFalse("This output happens after p.destroy(), so the streams are closed before it is read.",
			stderrHolder.value.contains("SIGTERM"));
	}

}
