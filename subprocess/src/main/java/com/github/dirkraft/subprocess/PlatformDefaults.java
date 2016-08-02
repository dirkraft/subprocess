package com.github.dirkraft.subprocess;

class PlatformDefaults {

	public static SubprocessBuilder<? extends SubprocessController> get() {
		String osName = System.getProperty("os.name");
		switch (osName) {
			case "Mac OS X":
				return new SubprocessBuilder<OsxSubprocessController>()
					.controllerDecorator(OsxSubprocessController::new);
			default:
				throw new InvalidOperationException("There is no platform default for " + osName);
		}
	}

}
