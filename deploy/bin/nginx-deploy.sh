#!/bin/bash
APP_NAME="agentseller"

BASE_DIR="${HOME}"
CURRENT_DIR="${PWD}"
TOMCAT_HOME="${BASE_DIR}/soft/tomcat"
NGINX_HOME="${BASE_DIR}/soft/nginx"

DEFAULT_LISTEN_PORT=${agentseller.tomcat.connector.port.base}

usage() {
	echo "Usage:"
	echo "    nginx-deploy.sh [-m mode] [-i instance] [-s server_name] [-c tomcat_count] [-r reload] [ -a action] [-h]"
	echo "Description:"
	echo "    mode - prod|dev|test, default: dev"
	echo "    instance - runtime instance name, runtime instance will be located in ~/${APP_NAME}/, default: runtime"
	echo "    server_name - the server host name, default: `hostname`"
	echo "    tomcat_count - the count of tomcat instance, default: 1"
	echo "    reload - 1:only reload nginx, default: 1"
	echo "    action - the action this nginx does, default: STARTUP"
	echo "    -h - show this help"
	exit -1
}

# 关闭Nginx
shutdown_nginx() {
	echo -e "\033[33m Shutting Down Nginx ${RUNTIME_DIR}/nginx \033[0m"
	${RUNTIME_DIR}/nginx/sbin/nginx -s stop -p ${RUNTIME_DIR}/nginx -c ${RUNTIME_DIR}/nginx/conf/nginx.conf
	NGINX_PID=`ps -ef | grep ${RUNTIME_DIR}/nginx | grep master | grep -v grep | awk '{print $2}'`
	[ ! -z $NGINX_PID ] && kill $NGINX_PID
}

# 生成upstreams.conf
write_upstreams_conf() {
	echo "upstream backends {" > ${RUNTIME_DIR}/nginx/conf/upstreams.conf
	for ((i=0;i<$COUNT;i++)); do
		echo "server localhost:$(expr $DEFAULT_LISTEN_PORT + $i) ;" >> ${RUNTIME_DIR}/nginx/conf/upstreams.conf
	done
	echo "}" >> ${RUNTIME_DIR}/nginx/conf/upstreams.conf
}


# 重新创建一个Nginx实例，并启动
start_a_clean_nginx() {
	echo -e "\033[33m Starting Nginx ${RUNTIME_DIR}/nginx \033[0m"
	mvn -f ./deploy/pom.xml resources:resources -P${MODE} -Dinstance.name=${INSTANCE} -Druntime.dir=${RUNTIME_DIR} -Dserver.name=${SERVER_NAME}
	
	rm -rf ${RUNTIME_DIR}/nginx
	mkdir -p ${RUNTIME_DIR}/nginx
	cp -rf ${NGINX_HOME}/* ${RUNTIME_DIR}/nginx/
	cp -rf ./deploy/target/nginx/* ${RUNTIME_DIR}/nginx
	
	write_upstreams_conf

	${RUNTIME_DIR}/nginx/sbin/nginx -p ${RUNTIME_DIR}/nginx -c ${RUNTIME_DIR}/nginx/conf/nginx.conf
}

# 刷新Nginx实例
reload_nginx() {
	echo -e "\033[33m Reloading Nginx ${RUNTIME_DIR}/nginx \033[0m"	
	mvn -f ./deploy/pom.xml resources:resources -P${MODE} -Dinstance.name=${INSTANCE} -Druntime.dir=${RUNTIME_DIR} -Dserver.name=${SERVER_NAME}
	
	mkdir -p ${RUNTIME_DIR}/nginx
	cp -rf ./deploy/target/nginx/* ${RUNTIME_DIR}/nginx
	
	write_upstreams_conf

	${RUNTIME_DIR}/nginx/sbin/nginx -s reload -p ${RUNTIME_DIR}/nginx -c ${RUNTIME_DIR}/nginx/conf/nginx.conf
}

#参数处理
MODE="dev"
INSTANCE="runtime"
SERVER_NAME=`hostname`
COUNT=1
RELOAD=1
ACTION="STARTUP"

while getopts "h:m:i:s:c:r:a:" arg
do
	case $arg in
		m) MODE=$OPTARG;;
		i) INSTANCE=$OPTARG;;
		s) SERVER_NAME=$OPTARG;;
		c) COUNT=$OPTARG;;
		r) RELOAD=$OPTARG;;
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

if [[ $ACTION = "SHUTDOWN" ]]; then
	shutdown_nginx
	exit 0
fi

sh ./deploy/target/bin/prepare-nginx-logs.sh

if [ $RELOAD -eq 0 ]; then
	shutdown_nginx
	start_a_clean_nginx
else
	# 如果当前没有运行中的nginx，不做reload，改为start
	NGINX_PID_FIND=`ps -ef | grep ${RUNTIME_DIR}/nginx | grep master | grep -v grep | awk '{print $2}'`
	if [ "$NGINX_PID_FIND"x = ""x ];then
		echo "There is no nginx running, start a new one."
		start_a_clean_nginx
	else
		reload_nginx
	fi
fi

