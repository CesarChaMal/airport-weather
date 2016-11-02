#!/usr/bin/env bash

#
# command line runner for the weather service REST endpoint
#

function cleanup() {
    kill ${SERVER_PID} ${CLIENT_PID}
    rm -f cp.txt
}

trap cleanup EXIT

#JAVA_HOME="/c/Java/jdk1.8.0_60"
#export JAVA_HOME
#CURRENT_PATH=$(pwd)

# mvn -DskipTests=true -Djetty.port=9090 jetty:run
# mvn -Dtest=com.crossover.trial.weather.services.WeatherEndpointTest dependency:build-classpath -Dmdep.outputFile=cp.txt 
# mvn test dependency:build-classpath -Dmdep.outputFile=cp.txt
# mvn test -Dmaven.test.failure.ignore=true dependency:build-classpath -Dmdep.outputFile=cp.txt
mvn -DskipTests=true compile dependency:build-classpath -Dmdep.outputFile=cp.txt

# using git-bash in windows
# sed -i 's/\\/\//g' cp.txt | sed -i 's/\C:/\/c/g' cp.txt | sed -i 's/\;/\:/g' cp.txt
# using cygwin in windows
#sed -e 's/\\/\//g' -e 's/\C:/\/cygdrive\/c/g' -e 's/\;/\:/g' -e 's/\s+/\ /g' 'cp.txt'

CLASSPATH="$(cat cp.txt):target/classes"
CLASSPATH="$(echo ${CLASSPATH} | sed -e 's/\\/\//g' -e 's/\C:/\/cygdrive\/c/g' -e 's/\;/\:/g' -e 's/\s+/\ /g')"
#CLASSPATH=$(sed -e 's/\\/\//g' -e 's/\C:/\/cygdrive\/c/g' -e 's/\;/\:/g' -e 's/\s+/\ /g' <<< $CLASSPATH)

#set PATH="$PATH:$JAVA_HOME/bin"
#export PATH

#echo ${PATH}
echo ${CLASSPATH}
#java -version

java -classpath "${CLASSPATH}" com.crossover.trial.execs.Server &
SERVER_PID=$$	

# nc only works in cygwin in windows, git bash does not have nc available
while ! nc localhost 9090 > /dev/null 2>&1 < /dev/null; do
    echo "$(date) - waiting for server at localhost:9090..."
    sleep 1
done

java -classpath "${CLASSPATH}" com.crossover.trial.execs.Client
CLIENT_PID=$$
cleanup
