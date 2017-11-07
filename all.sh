#!/bin/bash
svn up
sh build.sh -u false
[ $? -ne 0 ] && echo -e "\033[31m BUILD FAILED!!! \033[0m" && exit -1;
cd output
tar xzf agentseller.tgz
cd target
sh deploy.sh
cd ../../

