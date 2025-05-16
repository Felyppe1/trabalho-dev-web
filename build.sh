#!/bin/bash

WAR_NAME="exercicio.war"
TOMCAT_CONTAINER="trabalho-dev-web-tomcat"
TOMCAT_DEST="/usr/local/tomcat/webapps/$WAR_NAME"

echo "🧹 Limpando diretório target..."

sudo rm -rf target/

echo "🔧 Rodando build com Maven..."

docker run --rm \
  -v "$PWD":/app \
  -w /app \
  maven:3.8.5-eclipse-temurin-17 \
  mvn package

if [ $? -ne 0 ]; then
  echo "❌ Erro no build!"
  exit 1
fi

echo "📤 Copiando WAR para Tomcat..."

docker cp "$PWD/target/$WAR_NAME" "$TOMCAT_CONTAINER:$TOMCAT_DEST"

if [ $? -eq 0 ]; then
  echo "✅ Deploy concluído!"
else
  echo "❌ Falha ao copiar WAR!"
fi
