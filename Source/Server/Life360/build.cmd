#!/bin/sh
DEBUG=true
COMPRESS=true

ant clean #clean project

CMD="ant jar -Djavac.debug=$DEBUG -Djar.compress=$COMPRESS"
$CMD
echo Done by build command: $CMD
