package com.github.dirkraft.sh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;

class TempFileShOutStream implements ShOutStream {

	private static final String TMP_PFX = "com.github.dirkraft.sh-";

	private static final Logger LOG = LoggerFactory.getLogger(TempFileShOutStream.class);

	static TempFileShOutStream create(String suffix) {
		File tempFile = No.check(() -> File.createTempFile(TMP_PFX, suffix));
		return new TempFileShOutStream(tempFile);
	}

	private final File tempFile;

	private TempFileShOutStream(File tempFile) {
		this.tempFile = tempFile;
	}

	File getTempFile() {
		return tempFile;
	}

	@Override
	public String getUnread() {
		byte[] bytes = No.check(() -> Files.readAllBytes(tempFile.toPath()));
		LOG.trace("Read {} bytes into {}", bytes.length, tempFile);
		return new String(bytes);
	}

	@Override
	public void close() throws ShException {
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
