# Etapa 1: Build da aplicação
FROM maven:3.8.5-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY . .

RUN mvn clean package -U

# Etapa 2: Executar a aplicação com Tomcat
FROM tomcat:latest

# Copia o WAR gerado pelo Maven para o Tomcat
COPY --from=builder /app/target/trabalho-dev-web.war /usr/local/tomcat/webapps/

# Expõe a porta 8080
EXPOSE 8080

# Inicia o Tomcat
CMD ["catalina.sh", "run"]
