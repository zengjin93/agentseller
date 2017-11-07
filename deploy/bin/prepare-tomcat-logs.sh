#!/bin/bash
[ ! -d ${agentseller.logs.app.dir}/stat ] && mkdir -p ${agentseller.logs.app.dir}/stat
[ ! -d ${agentseller.logs.tomcat.dir} ] && mkdir -p ${agentseller.logs.tomcat.dir}

CURRENT_TIME=`date '+%Y%m%d_%H%M%S'`
mv ${agentseller.logs.tomcat.dir}/gc.log ${agentseller.logs.tomcat.dir}/gc.log.${CURRENT_TIME}
mv ${agentseller.logs.tomcat.file} ${agentseller.logs.tomcat.file}.${CURRENT_TIME}

