export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=7000,server=y,suspend=n"

nohup mvn jetty:run -X -Djetty.http.port=7071 -Dservice.registry.config.location=/root/workspace/hdp/reference-apps/iot-trucking-app/trucking-web-portal/src/main/resources/config/dev/registry -Dtrucking.activemq.host=10.10.1.141:8161 &

