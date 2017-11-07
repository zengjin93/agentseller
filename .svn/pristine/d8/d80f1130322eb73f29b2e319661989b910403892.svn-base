#!/bin/bash

TOMCAT_APP_PORT=${agentseller.tomcat.connector.port}

# Throughput Prefered
#CATALINA_OPTS="-server -Xmx1792m -Xms1792m -Xmn672m -Xss512k -XX:PermSize=256m -XX:MaxPermSize=256m -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCDateStamps -verbose:gc -Dsun.net.inetaddr.ttl=5 -Dapp.name=agentseller -Xloggc:${agentseller.logs.tomcat.dir}/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${agentseller.logs.tomcat.dir} -XX:ErrorFile=${agentseller.logs.tomcat.dir}/hs_err_pid%p.log -Djava.security.egd=file:/dev/./urandom"

# ResponseTime Prefered
CATALINA_MEM_OPTS=""
if [ "${run.mode}"x = "dev"x -o "${run.mode}"x = "test"x ]; then
    CATALINA_MEM_OPTS="${CATALINA_MEM_OPTS} -Xmx1024m -Xms1024m -Xmn380m -XX:PermSize=128m -XX:MaxPermSize=128m"
else
    CATALINA_MEM_OPTS="${CATALINA_MEM_OPTS} -Xmx1792m -Xms1792m -Xmn900m -XX:PermSize=256m -XX:MaxPermSize=256m"
fi
CATALINA_NORMAL_OPTS="-server -Xss512k -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSClassUnloadingEnabled
-XX:+PrintTenuringDistribution -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=68 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -verbose:gc -Dsun.net.inetaddr.ttl=30 -Dapp.name=agentseller -Xloggc:${agentseller.logs.tomcat.dir}/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${agentseller.logs.tomcat.dir} -XX:ErrorFile=${agentseller.logs.tomcat.dir}/hs_err_pid%p.log -Djava.security.egd=file:/dev/./urandom  -Dtomcat.count=${COUNT}"
CATALINA_OPTS="${CATALINA_MEM_OPTS} ${CATALINA_NORMAL_OPTS}"

if [ "${run.mode}"x = "dev"x -o "${run.mode}"x = "test"x ]; then
	DEBUG_PORT=$(expr $TOMCAT_APP_PORT + 100 )
	echo "Enable Debug Port on ${DEBUG_PORT}"
	CATALINA_OPTS="${CATALINA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${DEBUG_PORT}"
fi

export CATALINA_OPTS
export CATALINA_OUT="${agentseller.logs.tomcat.file}"
