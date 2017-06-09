#!/bin/sh

TODAY=`date +"%Y-%m-%d"`

rsync -aurv cmd/ root@103.254.13.25:/server/project/Life360/cmd/
rsync -aurv conf/ root@103.254.13.25:/server/project/Life360/conf/
rsync -aurv dist/ root@103.254.13.25:/server/project/Life360/dist/
