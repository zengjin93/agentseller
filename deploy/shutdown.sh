#!/bin/bash
APP_NAME="agentseller"

BASE_DIR="${HOME}"
CURRENT_DIR="${PWD}"
SOFT_DIR="${BASE_DIR}/soft"
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
	echo "    shutdown.sh [-m mode]  [-i instance] {-f config_filke} "
	echo "Description:"
	echo "    mode - prod|dev|test, default: dev"
	echo "    instance - runtime instance name, runtime instance will be located in ~/${APP_NAME}/, default: runtime"
	echo "    config_file - the properties file to be merged with app-config, default: none"
	exit -1
}

#参数处理
MODE="dev"
INSTANCE="runtime"
FILE=""

while getopts "m:i:f:" arg
do
	case $arg in
		m) MODE=$OPTARG;;
		i) INSTANCE=$OPTARG;;
		f) FILE=$OPTARG;;
		?) usage;;
	esac
done

RUNTIME_DIR="${BASE_DIR}/${APP_NAME}/${INSTANCE}"

#检查参数
if [ $MODE != "dev" -a $MODE != "test" -a $MODE != "prod" ]; then
	echo -e "\033[31m Running mode is not correct! \033[0m"
	usage
fi

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
echo "  Config File: $FILE"
echo "===================================================="

if [ ! "$FILE"x = ""x ]; then
	echo "Merging config files..."
	LIB="./${APP_NAME}.war/WEB-INF/lib"
	SH_UTIL_COMMON=`ls -1 ${LIB}/superhero-util-common-*.jar`
	$JAVA_HOME/bin/java -cp ${SH_UTIL_COMMON}:${LIB}/commons-lang3-3.1.jar com.baifubao.superhero.util.common.PropertiesUtil ./deploy/conf/app/${APP_NAME}-app-config.properties.${MODE} ${FILE} ./deploy/conf/app/${APP_NAME}-app-config.properties.${MODE}
fi
mvn -f ./deploy/pom.xml clean resources:resources -P${MODE} -Dinstance.name=${INSTANCE} -Druntime.dir=${RUNTIME_DIR}

sh ./deploy/target/bin/tomcat-deploy.sh -a SHUTDOWN -i ${INSTANCE} -m ${MODE}
sh ./deploy/target/bin/nginx-deploy.sh -a SHUTDOWN -i ${INSTANCE} -m ${MODE}
