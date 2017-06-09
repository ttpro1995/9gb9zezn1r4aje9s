#!/bin/sh

ENTRY_PATH=`readlink -f $0`

PROJECT_HOME=`dirname $ENTRY_PATH`
CMD_DIR=cmd
CONF_DIR=conf
JAR_DIR=dist
cd $PROJECT_HOME
#setup JAVA environment
#. $PROJECT_HOME/$CMD_DIR/_sys-env.sh

################################################################################

APP_NAME=Life360
APP_VER=""

################################################################################
#name of jar
if [ "x$JAR_NAME" = "x" ]; then

    if [ "x$APP_VER" != "x" ]; then
        JAR_NAME="$APP_NAME-$APP_VER"
    else
        JAR_NAME="$APP_NAME"
    fi
fi


TMP_DIR="/server/tmp/$APP_NAME"
LOG_DIR="/data/log/$APP_NAME"

#pid file
PID_FILE="$APP_NAME.pid"
if [ "x$PID_FILE" != "x" ]; then
    PID_PATH="$TMP_DIR/$PID_FILE"
fi

#run-log file
RUNLOG_FILE="$APP_NAME.log"
if [ "x$RUNLOG_FILE" != "x" ]; then
RUNLOG_PATH="$TMP_DIR/$RUNLOG_FILE"
fi

testLaunchService() {
	RUN_CMD="java -jar $PROJECT_HOME/$JAR_DIR/$JAR_NAME.jar"
	echo Run command: $RUN_CMD

	mkdir -p $TMP_DIR
}

launchService() {
	testLaunchService
	########## execute ##########
  $RUN_CMD 1>>"$RUNLOG_PATH" 2>>"$RUNLOG_PATH" &
	/bin/echo -n $! > "$PID_PATH"
}

checkService() {

	if [ -e "$PID_PATH" ]; then
    	_PID="`cat $PID_PATH`"
    	_PINF="`ps -fp $_PID | grep $_PID`"
    	if [ "x$_PINF" = "x" ]; then
    	       rm -f "$PID_PATH"
    	fi
	fi
}

cleanLog() {
	echo "Cleaning up: $TMP_DIR ..."
	rm -f $TMP_DIR/*.log

	echo "Cleaning up: $LOG_DIR ..."
	rm -f $LOG_DIR/*.log
	rm -f $LOG_DIR/*.log.*
}

printStatus() {
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~SATTUS~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
	if [ -e "$PID_PATH" ]; then
	echo "Application is running!"
  echo
	echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Process Info ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
	ps -fp `cat $PID_PATH` | grep ""
	else
	echo "Application stopped!"
	fi
}

printUsage() {
	echo "Usage: `basename $ENTRY_PATH` try|start|stop|restart|status|sysinfo|cll [production|development]"
	echo
	echo " The first option is service action:"
	echo " - try: print out arguments & environment for start program, the program will not be launched"
	echo " - start: launch the program"
	echo " - stop: kill the program"
	echo " - restart: kill the program first, then launch again the program"
	echo " - status: show the program is running or stopped"
	echo " - sysinfo: print out the system info"
	echo " - cll: clean log files of the program"
	echo
}

printSysInfo() {
	echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ System Info ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
	echo "Java Home $JAVA_HOME"
	echo "Java: $JAVA"
}

checkService


case "$1" in
  try)
    printSysInfo
    echo
    testLaunchService
    echo
    ;;
	start)
		if [ -e "$PID_PATH" ]; then
		echo "Application is already running!"
		echo
		exit 1
		fi
		printSysInfo
		echo
		launchService
		echo
		$ENTRY_PATH status
		;;
	stop)
    echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~STOP~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
		if [ ! -e "$PID_PATH" ]; then
		echo "Application already stopped!"
		echo
		exit 1
		fi
		##ok:stop it
		kill -9 `cat $PID_PATH`
		rm $PID_PATH -f
		echo "Stopped."
		echo
		;;
	restart)
		$ENTRY_PATH stop
		sleep 5
		$ENTRY_PATH start
		;;
	status)
		printStatus
		echo
		;;
	sysinfo)
		printSysInfo
		echo
		;;
	cll)
		cleanLog
		echo
		;;
	*)
		printUsage
		echo
		exit 1
esac































aw=a
