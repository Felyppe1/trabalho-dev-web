# Etapa 1: Build da aplicação usando Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o projeto para o container
COPY . .

# Executa o build (gera um .war no diretório target)
RUN mvn clean package -DskipTests

# Etapa 2: Executa o .war no Tomcat
FROM tomcat:10.1-jdk17

# Remove os webapps padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o .war gerado para o Tomcat
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Expõe a porta padrão do Tomcat
EXPOSE 8080

# Inicia o Tomcat
CMD ["catalina.sh", "run"]
