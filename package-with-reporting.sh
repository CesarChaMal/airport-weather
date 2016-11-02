#!/bin/bash -e
THIS_DIR="$(basename $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"
ARCHIVE=${THIS_DIR}/${THIS_DIR}.zip
#TARBALL=${THIS_DIR}/${THIS_DIR}.tgz

echo "Executing tests and creating archive ${ARCHIVE}"

# mvn -q test
mvn -q test compile package -Pwindows site:site site:deploy
mvn -q clean
pushd .. > /dev/null
jar cf ${ARCHIVE} ${THIS_DIR}/pom.xml /tmp/maven-report ${THIS_DIR}/logs ${THIS_DIR}/src ${THIS_DIR}/package-with-reporting.sh ${THIS_DIR}/run-ws.sh ${THIS_DIR}/assignment.md
popd > /dev/null
