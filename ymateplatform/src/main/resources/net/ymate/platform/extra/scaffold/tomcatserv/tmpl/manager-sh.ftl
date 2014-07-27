export CATALINA_HOME="${catalina_home}"

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CATALINA_HOME/lib

export CATALINA_BASE="${catalina_base}"

#export CATALINA_OPTS="-Xmx350M -Xms350M -XX:PermSize=64M -XX:MaxPermSize=64m -XX:+UseParallelGC"
export CATALINA_PID="$CATALINA_BASE/logs/catalina.pid"

$CATALINA_HOME/bin/catalina.sh $*