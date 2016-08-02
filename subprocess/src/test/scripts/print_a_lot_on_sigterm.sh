#!/usr/bin/env bash

function print_alot() {
	for i in `seq 1 100` ; do
		echo "Line number $i"
	done
	echo $1
}

trap ">&2 print_alot SIGTERM ; exit 0" SIGTERM

print_alot Hello

while true ; do
	sleep 0.1
done
