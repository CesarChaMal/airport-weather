#!/usr/bin/env bash

#
# command line runner for the weather service REST endpoint
#

function cleanup() {
    kill ${SERVER_PID} ${CLIENT_PID}
    rm -f cp.txt
}

trap cleanup EXIT

# mvn test dependency:build-classpath -Dmdep.outputFile=cp.txt
# mvn test -Dmaven.test.failure.ignore=true dependency:build-classpath -Dmdep.outputFile=cp.txt
mvn -DskipTests compile dependency:build-classpath -Dmdep.outputFile=cp.txt

CLASSPATH=$(cat cp.txt):target/classes
java -classpath ${CLASSPATH} com.crossover.trial.execs.Server &
SERVER_PID=$$

while ! nc localhost 9090 > /dev/null 2>&1 < /dev/null; do
    echo "$(date) - waiting for server at localhost:9090..."
    sleep 1
done

java -classpath ${CLASSPATH} com.crossover.trial.execs.Client
CLIENT_PID=$$
cleanup
