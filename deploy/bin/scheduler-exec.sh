#!bin/bash
#TIME=`date +"%Y-%m-%d %H:%M:%s"`

usage() {
	echo "Usage:"
	echo "    scheduler-exec.sh [-t target] [-p port] [-n taskname] [-s parameter] [-h]"
	echo "Description:"
	echo "target- the target you want to invoke in "
	echo "port-the port which service at  "
	echo "taskname- the type of task you want to process"
	echo "parameter- the paramater you want to use in your task"
	echo " -h - show this help"
	exit -1
}

TARGET=""
PORT=""
TASKNAME=""
PARAMETER=""

while getopts "t:p:n:s:h:" arg
do
	case $arg in
		t) TARGET=$OPTARG;;
		p) PORT=$OPTARG;;
		s) PARAMETER=$OPTARG;;
		n) TASKNAME=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

#²ÎÊý¼ì²é
if [[ "$TARGET"x  == ""x ]]; then
	echo "Target can't be null"
	exit -1
fi

if [[ "$PORT"x == ""x ]]; then
	echo "Port can't be null"
	exit -1
fi

if [[ "$TASKNAME"x == ""x ]]; then
	echo "taskname can't be null"
	exit -1
fi

TIME_SECOND=`date +%s`
TIME_MISECOND="${TIME_SECOND}000"
JSON_STRING="{\"invokeTime\":\"$TIME_MISECOND\",\"parameters\":\"$PARAMETER\"}"

echo "target is $TARGET"
echo "port is $PORT"
echo "taskname is $TASKNAME"
echo "post body is $JSON_STRING"

curl -X POST -H "Content-Type: application/json" -d $JSON_STRING  http://$TARGET:$PORT/_scheduler/$TASKNAME
