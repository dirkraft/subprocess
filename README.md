subprocess
----------

A simpler API to running subprocesses from Java and reading their
output.

I got really tired of Process, ProcessBuilder, and everything that
you have to do to `echo Hello world` and reliably collect it.
I'm hiding some of that nastiness behind this library.



### Usage

build.gradle

	repositories {
	    maven {
	        url 'https://oss.sonatype.org/content/repositories/snapshots/'
	    }
	}
	
	dependencies {
	    compile group: 'com.github.dirkraft.subprocess', name: 'subprocess', version: '0.0.1-SNAPSHOT'
	}

