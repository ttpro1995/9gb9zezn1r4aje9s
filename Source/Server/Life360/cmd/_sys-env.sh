#!/bin/sh


if [ "x$JAVA_NAME" = "x" ]; then
JAVA_NAME=`ls /server/java | grep jdk | grep x64 | tail -1`
fi

JAVA_HOME="/server/java/$JAVA_NAME"
JAVA="$JAVA_HOME/bin/java"
