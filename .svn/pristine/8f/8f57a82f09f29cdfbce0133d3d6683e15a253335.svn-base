#!/bin/bash
BNS_SERVICE_NAME="${app.bns.service.name}"

usage() {
	echo "Usage:"
	echo "    gen-bns-access.sh [-n nginx-access] [-h]"
	echo "Description:"
	echo "    nginx-access - path of generated nginx-access, default: ./deploy/conf/access/bns-nginx.conf"
	echo "    -h - show this help"
	exit -1
}

NGINX_WHITELIST="./deploy/conf/access/bns-nginx.conf"

while getopts "h:n:" arg
do
	case $arg in
		n) NGINX_WHITELIST=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

get_auth_instance_by_service -i ${BNS_SERVICE_NAME} | awk '{print "allow "$2";"}' > $NGINX_WHITELIST