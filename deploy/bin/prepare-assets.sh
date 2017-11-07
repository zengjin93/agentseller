#!/bin/bash
APP_NAME="agentseller"

BASE_DIR="${HOME}"
INSTANCE="runtime"

usage() {
	echo "Usage:"
	echo "    prepare-assets.sh [-i instance] [-h]"
	echo "Description:"
	echo "    instance - runtime instance name, runtime instance will be located in ~/${APP_NAME}/, default: runtime"
	exit -1
}

while getopts "h:i:" arg
do
	case $arg in
		i) INSTANCE=$OPTARG;;
		h) usage;;
		?) usage;;
	esac
done

RUNTIME_DIR="${BASE_DIR}/${APP_NAME}/${INSTANCE}"

[ ! -d ${RUNTIME_DIR}/webroot ] && mkdir -p ${RUNTIME_DIR}/webroot

echo "Linking ${assets.path} to ${RUNTIME_DIR}/webroot/assets"
[ -L ${RUNTIME_DIR}/webroot/assets ] && unlink ${RUNTIME_DIR}/webroot/assets
[ -d ${RUNTIME_DIR}/webroot/assets ] && rm -rf ${RUNTIME_DIR}/webroot/assets
ln -s ${assets.path} ${RUNTIME_DIR}/webroot/assets

echo "Linking ${cms.path} to ${template.path}/cms"
[ -L ${template.path}/cms ] && unlink ${template.path}/cms
[ -d ${template.path}/cms ] && rm -rf ${template.path}/cms
ln -s ${cms.path} ${template.path}/cms
