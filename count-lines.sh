#!/bin/bash
BLK=/tmp/daikokusrcblk
rm $BLK 2>/dev/null
for i in `find src daikokuTest-test/src|grep .java`; do cat $i >> $BLK; done
wc -l $BLK
