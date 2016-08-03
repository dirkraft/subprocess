#!/usr/bin/env bash

trap ">&2 echo SIGHUP ; exit 0" SIGHUP

trap ">&2 echo SIGTERM ignored" SIGTERM

while true ; do
	sleep 0.1
done
