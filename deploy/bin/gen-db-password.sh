#!/bin/sh
usage() {
	echo "gen-db-password.sh <key|pass>"
	echo -e "\tkey: generate RSA key"
	echo -e "\tpass: generate encrypted password"
	echo "gen-db-password.sh key [filepath]"
	echo "gen-db-password.sh pass <plaintext> [path to key file]"
	exit -1
}

REPO="${HOME}/.m2/repository"
SH_DB_VERSION=`ls -1 ${REPO}/com/baifubao/superhero/util/superhero-util-storage-db/`
CP="${REPO}/com/baifubao/superhero/util/superhero-util-storage-db/${SH_DB_VERSION}/superhero-util-storage-db-${SH_DB_VERSION}.jar:${REPO}/com/alibaba/druid/1.0.0/druid-1.0.0.jar"

if [ $# -lt 1 ]; then
	usage
elif [ $1 = "key" ]; then
	java -cp $CP com.baifubao.superhero.util.storage.db.tools.KeyPairGenerator $2
elif [ $1 = "pass" -a $# -lt 2 ]; then
	usage
elif [ $1 = "pass" ]; then
	java -cp $CP com.baifubao.superhero.util.storage.db.tools.EncryptedPasswordGenerator $2 $3
else
	usage
fi
