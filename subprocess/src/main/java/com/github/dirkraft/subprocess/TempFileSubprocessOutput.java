package com.github.dirkraft.subprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;

public class TempFileSubprocessOutput implements SubprocessOutput {

	private static final String TMP_PFX = "com.github.dirkraft.sh-";

	private static final Logger LOG = LoggerFactory.getLogger(TempFileSubprocessOutput.class);
	private final File tempFile;

	private TempFileSubprocessOutput(File tempFile) {
		this.tempFile = tempFile;
	}

	public static TempFileSubprocessOutput create(String suffix) {
		File tempFile = No.check(() -> File.createTempFile(TMP_PFX, suffix));
		return new TempFileSubprocessOutput(tempFile);
	}

	public File getTempFile() {
		return tempFile;
	}

	@Override
	public String getUnread() {
		byte[] bytes = No.check(() -> Files.readAllBytes(tempFile.toPath()));
		LOG.trace("Read {} bytes into {}", bytes.length, tempFile);
		return new String(bytes);
	}

	@Override
	public void close() throws SubprocessException {
		if (!tempFile.delete()) {
			LOG.warn("Failed to delete file: {}", tempFile);
		}
	}

	@Override
	public String toString() {
		return "TempFileShOutStream{" +
			"tempFile=" + tempFile +
			'}';
	}
}
