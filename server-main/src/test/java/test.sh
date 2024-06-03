#!/bin/bash
# 唯一进程标识
PROCESS_IDENTIFIER="dsp-mdbserver-0.0.1-SNAPSHOT.jar"
# 使用 pgrep -f 来检查进程是否存在
if pgrep -f "$PROCESS_IDENTIFIER" > /dev/null
then
    echo "Process is running."
    exit 0
else
    echo "Process is not running."
    exit 1
fi