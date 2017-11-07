#!/bin/bash
usage() {
	echo "Usage:"
	echo "	build.sh [-u upload]"
	echo "Description:"
	echo "	upload - true|false, upload to maven repo or not, default: true"
	exit -1
}

APP_NAME="agentseller"
UPLOAD="true"

while getopts "h:u:" arg
do
	case $arg in
		u) UPLOAD=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

#准备环境变量
SOFT_DIR="${HOME}/soft"
MAVEN_OPTS="-Xms512m -Xmx512m"
export JAVA_HOME=$JAVA_HOME_1_6
export MAVEN_OPTS

if [ -d ${SOFT_DIR}/java ]; then
	# 这里只装了JRE，但MAVEN要用JDK，所以设�?下，原则上JAVA_HOME不能设JRE
	# 以后如果装JDK就只设JDK
	export JAVA_HOME=${SOFT_DIR}/java
	export JRE_HOME=${SOFT_DIR}/java
fi

if [ -d ${SOFT_DIR}/maven ]; then
	export PATH=$PATH:${SOFT_DIR}/maven/bin
fi

#编译打包
rm -rf ./output
mkdir -p ./output
rm -rf ~/.m2/repository/com/baifubao
mvn clean package -Dmaven.test.skip=true
[ $? -ne 0 ] && echo -e "\033[31m BUILD FAILED!!! \033[0m" && exit -1;
svn info > ./deploy/version.txt
echo "Build Date: `date`" >> ./deploy/version.txt
mvn -f ./deploy/pom.xml clean assembly:single 
mv ./deploy/target/${APP_NAME}-deploy-1.0-SNAPSHOT-tgz.tar.gz ./output/${APP_NAME}.tgz

#Maven Upload
if [ "$UPLOAD"x = "true"x ]; then
	echo "Enable when needed!"
#	mvn deploy:deploy-file -DgroupId=com.baidu.agentseller -DartifactId=agentseller-service-api -Dversion=1.0-SNAPSHOT -Dpackaging=jar -Dmaven.test.skip=true -Dfile=app/service/api/target/agentseller-service-api-1.0-SNAPSHOT.jar -Dsources=app/service/api/target/agentseller-service-api-1.0-SNAPSHOT-sources.jar -Durl=http://maven.scm.baidu.com:8081/nexus/content/repositories/Baidu_Local/ -DrepositoryId=Baidu_Local
else
	echo "Bypass Maven deploy phrase."
fi

echo -e "\033[32m BUILD SUCCESSFUL!!! \033[0m"
