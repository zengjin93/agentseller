#!/bin/bash
APP_NAME="agentseller"

BASE_DIR="${HOME}"
CURRENT_DIR="${PWD}"
TOMCAT_HOME="${BASE_DIR}/soft/tomcat"
NGINX_HOME="${BASE_DIR}/soft/nginx"

DEFAULT_SHUTDOWN_PORT=${agentseller.tomcat.server.port.base}
DEFAULT_LISTEN_PORT=${agentseller.tomcat.connector.port.base}

usage() {
	echo "Usage:"
	echo "    deploy.sh [-m mode] [-i instance] [-s server_name] [-c tomcat_count] [-a action] [-h]"
	echo "Description:"
	echo "    mode - prod|dev|test, default: dev"
	echo "    instance - runtime instance name, runtime instance will be located in ~/${APP_NAME}/, default: runtime"
	echo "    server_name - the server host name, default: `hostname`"
	echo "    tomcat_count - the count of tomcat instance, default: 1"
	echo "    action - the action this script do, default: STARTUP"
	exit -1
}

#通过不停的监听tomcat catalina里面的Server startup语句来判断当前tomcat是否启动
#函数返回值为0的话,表明超时了且没有检测到启动;如果返回值为1的话,表明当前Tomcat已经启动
check_tomcat_startup(){
    GREP_FLAG=1;
    GREP_COUNT=40;
    sleep 3s
    LISTEN_PORT=$1;

    echo "find \"Server startup\" in  ~/var/${APP_NAME}/logs/runtime/tomcat_${1}/catalina.out"
    while [[ $GREP_FLAG == 1 ]]
    do
     grep  "Server startup"  ~/var/${APP_NAME}/logs/runtime/tomcat_${1}/catalina.out
     GREP_FLAG=$?
     GREP_COUNT=$[GREP_COUNT - 1];

     if [[ $GREP_FLAG == 1 ]];then
            echo -e "\033[33m ${APP_NAME}:${1} has not been started yet,grep_count=${GREP_COUNT} \033[0m"
     elif  [[ $GREP_FLAG == 0 ]];then
            echo -e "\033[32m ${APP_NAME}:${1} Start \033[0m"
            return 1
     else
            echo "grep Error,may be you grep the error directory"
            return 2;
     fi

     if [[ $GREP_COUNT == 0 ]];then
            echo -e "\033[31m Start ${APP_NAME}:${1} Time Out \033[0m"
            return 0
     fi

     sleep 3s
    done
}
# 关闭Tomcat
# 参数：Tomcat实例安装位置，Nginx实例位置
shutdown_tomcat() {
	if [ $2 ]; then
		echo -e "\033[33m Removing localhost:$2 from Nginx \033[0m"
		sed -i "s/server localhost:${2}/server localhost:${2} down/g" $NGINX_INSTANCE/conf/upstreams.conf
		${NGINX_INSTANCE}/sbin/nginx -s reload -p ${NGINX_INSTANCE} -c ${NGINX_INSTANCE}/conf/nginx.conf
		sleep 3s
	fi

	echo -e "\033[33m Shuttng Down Tomcat $1 \033[0m"

	if [ -d $1 ]; then
		chmod +x ${1}/bin/catalina.sh
		sh ${1}/bin/shutdown.sh
		sleep 3s
	fi
	TOMCAT_PID=`ps -ef | grep $1 | grep -v grep | awk '{print $2}'`	
	if [ ! -z $TOMCAT_PID ]; then
		kill $TOMCAT_PID
		sleep 3s
	fi
	TOMCAT_PID=`ps -ef | grep $1 | grep -v grep | awk '{print $2}'`	
	if [ ! -z $TOMCAT_PID ]; then
		kill -9 $TOMCAT_PID
		sleep 3s
	fi
}

# 重新创建一个Tomcat实例，并启动
# 参数：关闭端口号，监听端口号，Tomcat实例安装位置
start_a_clean_tomcat() {
	echo -e "\033[33m Preparing Tomcat in $3 \033[0m"
	rm -rf $3

	mvn -f ./deploy/pom.xml resources:resources -P${MODE} -Dinstance.name=${INSTANCE} -Druntime.dir=${RUNTIME_DIR} -Dserver.name=${SERVER_NAME} -Dtomcat.server.port=$1 -Dtomcat.connector.port=$2 -Dtomcat.count=${COUNT}
	echo “fffffffffffffffffffffffffff$1$2$3”
	mkdir -p $3
	cp -rf ${TOMCAT_HOME}/* $3
	rm -rf ${3}/webapps/*
	rm -rf ${3}/conf/tomcat-users.xml
	cp -rf ./deploy/target/tomcat/* $3
	cp -rf ${CURRENT_DIR}/${APP_NAME}.war ${3}/webapps/ROOT 

	mkdir -p ${3}/webapps/ROOT/WEB-INF/classes/
	cp ./deploy/target/conf/db/${APP_NAME}-db-config.properties.${MODE} ${3}/webapps/ROOT/WEB-INF/classes/${APP_NAME}-db-config.properties
	cp ./deploy/target/conf/app/${APP_NAME}-app-config.properties.${MODE} ${3}/webapps/ROOT/WEB-INF/classes/${APP_NAME}-app-config.properties
	cp ./deploy/target/conf/logback.xml ${3}/webapps/ROOT/WEB-INF/classes/logback.xml
	cp ${3}/webapps/ROOT/WEB-INF/lib/*slf*.jar ${3}/lib/
	#cp ${3}/webapps/ROOT/WEB-INF/lib/logback*.jar ${3}/lib/

	sh ./deploy/target/bin/prepare-tomcat-logs.sh
	chmod +x ${3}/bin/catalina.sh
	sh ${3}/bin/startup.sh

	check_tomcat_startup $2
	if [[ $? == 1 ]];then
		echo -e "\033[33m Adding localhost:$2 to Nginx \033[0m"
		sed -i "s/server localhost:${2} down/server localhost:${2}/g" $NGINX_INSTANCE/conf/upstreams.conf
		${NGINX_INSTANCE}/sbin/nginx -s reload -p ${NGINX_INSTANCE} -c ${NGINX_INSTANCE}/conf/nginx.conf
	fi
}

#参数处理
MODE="dev"
INSTANCE="runtime"
SERVER_NAME=`hostname`
COUNT=1
ACTION="STARTUP"

while getopts "h:m:i:s:c:a:" arg
do
	case $arg in
		m) MODE=$OPTARG;;
		i) INSTANCE=$OPTARG;;
		s) SERVER_NAME=$OPTARG;;
		c) COUNT=$OPTARG;;
		a) ACTION=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

RUNTIME_DIR="${BASE_DIR}/${APP_NAME}/${INSTANCE}"

#检查参数
if [ $MODE != "dev" -a $MODE != "test" -a $MODE != "prod" ]; then
	echo -e "\033[31m Running mode is not correct! \033[0m"
	usage
fi

LAST_COUNT=`ls -1 ${RUNTIME_DIR} | grep -c 'tomcat'`

if [[ $ACTION = "SHUTDOWN" ]]; then
	for t in `ls -1 ${RUNTIME_DIR} | sort -r | grep 'tomcat'`; do
		echo -e "\033[31m Shutting down Tomcat ${RUNTIME_DIR}/${t} \033[0m"
		shutdown_tomcat ${RUNTIME_DIR}/${t}
	done
	exit
fi

min=`ls -1 ${RUNTIME_DIR} | grep 'tomcat_*' | sort -u | head -1 | awk -F_ '{print $2}'`
if [[ $DEFAULT_LISTEN_PORT -ne min ]]; then
	for t in `ls -1 ${RUNTIME_DIR} | sort -r | grep 'tomcat'`; do
		echo -e "\033[31m Removing ALL Tomcat ${RUNTIME_DIR}/${t} \033[0m"
		shutdown_tomcat ${RUNTIME_DIR}/${t}
		rm -rf ${RUNTIME_DIR}/${t}
	done
fi

if [ $COUNT -lt $LAST_COUNT ]; then
	for t in `ls -1 ${RUNTIME_DIR} | sort -r | grep 'tomcat' | head -$(expr $LAST_COUNT - $COUNT)`; do
		echo -e "\033[31m Removing useless Tomcat ${RUNTIME_DIR}/${t} \033[0m"
		shutdown_tomcat ${RUNTIME_DIR}/${t}
		rm -rf ${RUNTIME_DIR}/${t}
	done
fi

NGINX_INSTANCE="${RUNTIME_DIR}/nginx"

for ((i=0;i<$COUNT;i++)); do 
	SHUTDOWN_PORT=$(expr $DEFAULT_SHUTDOWN_PORT + $i)
	LISTEN_PORT=$(expr $DEFAULT_LISTEN_PORT + $i)
	TOMCAT_INSTANCE="${RUNTIME_DIR}/tomcat_${LISTEN_PORT}"

	shutdown_tomcat $TOMCAT_INSTANCE $LISTEN_PORT
	start_a_clean_tomcat $SHUTDOWN_PORT $LISTEN_PORT $TOMCAT_INSTANCE
done

