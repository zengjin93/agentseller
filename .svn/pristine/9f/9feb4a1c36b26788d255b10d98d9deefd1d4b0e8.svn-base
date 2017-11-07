#!/bin/bash
APP_NAME="agentseller"
#待修改成线上配置
APP_BNS=""
BASE_DIR="${HOME}"
CURRENT_DIR="${PWD}"
SOFT_DIR="${BASE_DIR}/soft"
TOMCAT_HOME="${SOFT_DIR}/tomcat"
NGINX_HOME="${SOFT_DIR}/nginx"
ARRAY_MODE=(prod dev)

if [ -d ${SOFT_DIR}/java ]; then
	# 这里只装了JRE，但MAVEN要用JDK，所以设一下，原则上JAVA_HOME不能设JRE
	# 以后如果装JDK就只设JDK
	export JAVA_HOME=${SOFT_DIR}/java
	export JRE_HOME=${SOFT_DIR}/java
fi

if [ -d ${SOFT_DIR}/maven ]; then
	export PATH=$PATH:${SOFT_DIR}/maven/bin
fi

usage() {
	echo "Usage:"
	echo "    deploy.sh [-m mode] [-i instance] [-s server_name] [-c tomcat_count] [-r reload] [-f config_file] [-h]"
	echo "Description:"
	echo "    mode - prod|dev|test, default: dev"
	echo "    instance - runtime instance name, runtime instance will be located in ~/${APP_NAME}/, default: runtime"
	echo "    server_name - the server host name, default: `hostname`"
	echo "    tomcat_count - the count of tomcat instance, default: 1"
	echo "    reload - 1:only reload nginx, 0:start nginx, default: 1"
	echo "    config_file - the properties file to be merged with app-config, default: none"
	echo "    -h - show this help"
	exit -1
}
setDefaultMode() {
	IP=`/sbin/ifconfig | grep "inet addr" | awk -F: '{print $2}' | awk '{print $1}' | grep -v 127.0.0.1`
	if [[ "$APP_BNS"x = ""x ]]; then 
		return 1
	fi
	for t in `get_instance_by_service $APP_BNS  -a | awk '{print $2}'`;do
		if [[ "$IP"x = "$t"x ]]; then
			echo "this machine's ip is $IP, it is in $APP_BNS in bns_list, so switch this mode to prod"
			return 0
		fi
	done
	return 1
}

#参数处理
setDefaultMode;
MODE=${ARRAY_MODE[$?]}
INSTANCE="runtime"
SERVER_NAME=`hostname`
COUNT=1
RELOAD=1
FILE=""

while getopts "h:m:i:s:c:r:f:" arg
do
	case $arg in
		m) MODE=$OPTARG;;
		i) INSTANCE=$OPTARG;;
		s) SERVER_NAME=$OPTARG;;
		c) COUNT=$OPTARG;;
		r) RELOAD=$OPTARG;;
		f) FILE=$OPTARG;;
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

if [ "$FILE"x = ""x -a -f $RUNTIME_DIR/app-config.properties ]; then
	echo "Config file is not specified, but ${RUNTIME_DIR}/app-config.properties is FOUND!"
	FILE="${RUNTIME_DIR}/app-config.properties"
fi

#打印信息
echo "===================================================="
echo "  Application: $APP_NAME"
echo "----------------------------------------------------"
echo "  Mode: $MODE"
echo "  Instance: $INSTANCE"
echo "  Server Name: $SERVER_NAME"
echo "  Runtime: $RUNTIME_DIR"
echo "  Config File: $FILE"
echo "----------------------------------------------------"
echo "  Tomcat Count: $COUNT"
echo "  Last Time Tomcat Count: $LAST_COUNT"
echo "  Only Reload Nginx: $RELOAD"
echo "----------------------------------------------------"
echo "  Tomcat: $TOMCAT_HOME"
echo "  Nginx: $NGINX_HOME"
echo "===================================================="

#获取当前的host名字
HOST_NAME=`hostname`
LOCATION=`echo "$HOST_NAME" | awk -F '.' '{print $(NF-2)}'`
LOCATION_FILE_NAME="appconfig.${LOCATION}"
if [ $MODE = "prod" ]; then
	if [ ! "$LOCATION"x = ""x ]; then
    		if [ -f "./deploy/conf/app/location/${LOCATION_FILE_NAME}" ]; then
        		echo -e "\033[33m Merging Location config files,now we use ./deploy/conf/app/location/${LOCATION_FILE_NAME} depend on the Location ${LOCATION}...\033[0m"
        		LIB="./${APP_NAME}.war/WEB-INF/lib"
	    		#暂时注释掉
	    		#SH_UTIL_COMMON=`ls -1 ${LIB}/superhero-util-common-*.jar`
	    		SH_UTIL_COMMON=`ls -1 ${LIB}/agentseller-base-*.jar`
	    		$JAVA_HOME/bin/java -cp ${SH_UTIL_COMMON}:${LIB}/commons-lang3-3.1.jar com.baidu.agentseller.base.util.common.PropertiesUtil ./deploy/conf/app/${APP_NAME}-app-config.properties.${MODE} ./deploy/conf/app/location/${LOCATION_FILE_NAME} ./deploy/conf/app/${APP_NAME}-app-config.properties.${MODE}
    		else
        		echo -e "\033[33m ./deploy/conf/app/location/${LOCATION_FILE_NAME} is not a valid file \033[0m"
    		fi

	fi
fi
if [ ! "$FILE"x = ""x ]; then
	echo "Merging config files..."
	LIB="./${APP_NAME}.war/WEB-INF/lib"
	SH_UTIL_COMMON=`ls -1 ${LIB}/superhero-util-common-*.jar`
	${JAVA_HOME}/bin/java -cp ${SH_UTIL_COMMON}:${LIB}/commons-lang3-3.1.jar com.baidu.agentseller.base.util.common.PropertiesUtil ./deploy/conf/app/${APP_NAME}-app-config.properties.${MODE} ${FILE} ./deploy/conf/app/${APP_NAME}-app-config.properties.${MODE}
fi

mvn -f ./deploy/pom.xml clean resources:resources -P${MODE} -Dinstance.name=${INSTANCE} -Druntime.dir=${RUNTIME_DIR} -Dserver.name=${SERVER_NAME} -Dtomcat.count=${COUNT}

# 生产模式下从BNS获取授权白名单
# 为了保证所有nginx的配置都一样，所以提前生成文件，而不是
if [ $MODE = "prod" ]; then
	sh ./deploy/target/bin/gen-bns-access.sh
fi

if [ $COUNT -ge $LAST_COUNT ]; then
	sh ./deploy/target/bin/tomcat-deploy.sh -m ${MODE} -i ${INSTANCE} -s ${SERVER_NAME} -c ${COUNT}
	sh ./deploy/target/bin/nginx-deploy.sh -m ${MODE} -i ${INSTANCE} -s ${SERVER_NAME} -c ${COUNT} -r ${RELOAD}
else
	sh ./deploy/target/bin/nginx-deploy.sh -m ${MODE} -i ${INSTANCE} -s ${SERVER_NAME} -c ${COUNT} -r ${RELOAD}
	sh ./deploy/target/bin/tomcat-deploy.sh -m ${MODE} -i ${INSTANCE} -s ${SERVER_NAME} -c ${COUNT}
fi

sh ./deploy/target/bin/prepare-assets.sh -i ${INSTANCE}



cp -f ./deploy/target/bin/check-service.sh ./check-service.sh
sh ./check-service.sh

exit $?
