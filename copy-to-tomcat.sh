#!/bin/bash

WAR_NAME="trabalho-dev-web.war"

mvn clean package

cp ./target/${WAR_NAME} ${SDKMAN_DIR}/candidates/tomcat/current/webapps