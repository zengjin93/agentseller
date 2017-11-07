#!/bin/bash
[ ! -d ${agentseller.logs.nginx.dir} ] && mkdir -p ${agentseller.logs.nginx.dir}

CURRENT_TIME=`date '+%Y%m%d_%H%M%S'`

kill -USR1 `cat ${agentseller.logs.nginx.dir}/nginx.pid`
