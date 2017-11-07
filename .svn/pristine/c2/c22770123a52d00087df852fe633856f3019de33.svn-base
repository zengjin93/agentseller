#!/bin/bash
PORT="${agentseller.nginx.service.listen.port}"

RESULT=`curl -s -X GET http://localhost:${PORT}/_health/status | grep "agentseller" | grep "OK"`
if [ $? -ne 0 ]; then 
	echo -e "\033[31m Exception occured when checking health status! \033[0m"
	exit 1
elif [ "$RESULT"x = ""x ]; then
	echo -e "\033[31m Something maybe WRONG with the services! \033[0m"
	exit 1
else
	echo -e "\033[32m Service OK! \033[0m"
fi
