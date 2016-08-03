subprocess
----------

A simpler API to running subprocesses from Java and reading their
output.

[![Build Status](https://travis-ci.org/dirkraft/subprocess.svg?branch=master)](https://travis-ci.org/dirkraft/subprocess)

I got really tired of Process, ProcessBuilder, and everything that
you have to do to `echo Hello world` and reliably collect it.
I'm hiding some of that nastiness behind this library, adding features
as they become needed in other projects.



### Usage ###

build.gradle

	repositories {
	    maven {
	        url 'https://oss.sonatype.org/content/repositories/snapshots/'
	    }
	}
	
	dependencies {
	    compile group: 'com.github.dirkraft.subprocess', name: 'subprocess', version: '0.0.1-SNAPSHOT'
	}

Generally, I'm just starting/stopping commands and checking output.

	SubprocessResult result = Subprocess.getOutput("ls", "-al");
	// Usually do something more interesting than the following...
	System.out.println(result.getStdout());



### Why ###

I was bootstrapping a postgres testdb for some automated tests in
another project and was having trouble shutting down postgres because
it prints output on SIGTERM and Process likes to close InputStreams
immediately, rather than waiting for the process to exit (I think).

So once I was able to actually capture and analyze the output coming
out on SIGTERM (the signal sent by `Process.destroy()` on OSX),
I saw the db was hanging which I eventually deduced
to open connections. Postgres won't close any open connections on
SIGTERM and will wait. Because this is for local testing, using a
special shutdown signaler I can tell postgres to go ahead and shutdown 
regardless of open connections via SIGINT.

	builder.shutdownSignaler(new PosixAwareShutdownSignaler("kill", "-SIGINT"))

So now I can start and stop a freshly-created postgres as part of a 
test run.

