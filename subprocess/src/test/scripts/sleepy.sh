#!/usr/bin/env bash

sleep 0.1

trap ">&2 echo Yawwwn ; sleep 0.5 ; exit 0" SIGTERM

echo "Oh hi. Didn't see you there."

while true ; do
	sleep 0.1
done
