#!/usr/bin/env sh

# Implementation of the gradle-wrapper script for POSIX systems.

# Attempt to set APP_HOME
# Resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

APP_PATH=`dirname "$PRG"`
APP_HOME=`cd "$APP_PATH" && pwd`

exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
