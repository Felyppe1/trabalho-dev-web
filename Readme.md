# DevBank

DevBank é um sistema bancário simples desenvolvido como parte do trabalho da disciplina **Desenvolvimento Web**. O projeto utiliza **Java com Servlets e JSP**, conectando-se a um banco de dados PostgreSQL para simular operações básicas bancárias, como login, visualização de extrato e movimentações financeiras.

## Tecnologias utilizadas

- Java 17+
- Servlets & JSP
- Apache Tomcat
- PostgreSQL
- Docker

## Como rodar o projeto

### Usando Dev Container (faça isso caso queira desenvolver no projeto)

#### Pré-requisitos

Antes de começar, certifique-se de que você possui:

- [Docker](https://www.docker.com/) instalado e em execução
- [Visual Studio Code (VS Code)](https://code.visualstudio.com/) instalado
- Extensão **[Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)** instalada no VS Code

#### 1. Configurar variáveis de ambiente:

Copie o arquivo de exemplo `.env.example` para o `.env` dentro da pasta `/.devcontainer` e preencha as variáveis necessárias:
```bash
cp .env.example ./.devcontainer/.env
# Edite o arquivo .env com os valores apropriados
```

#### 2. Abrir o projeto no container
No VS Code, pressione `Ctrl + Shift + P` (ou `Cmd + Shift + P` no macOS), digite:
```txt
Dev Containers: Rebuild and Reopen in Container
```
E selecione a opção para reconstruir e abrir o projeto dentro do container.

#### 3. Liberar permissão de arquivos

```bash
chmod +x /usr/local/sdkman/candidates/tomcat/current/bin/*.sh
```

#### 4. Buildar e jogar no Tomcat

```bash
./build.devcontainer.sh
```

#### 5. Iniciar o tomcat

```bash
/usr/local/sdkman/candidates/tomcat/current/bin/catalina.sh start
```

#### 6. Acessar a aplicação

Abra no navegador: http://localhost:8080

#### Caso queira alterar o banco (rodar fora do Dev Container):

```bash
docker cp init.sql trabalho-dev-web-db:/init.sql
docker exec -it trabalho-dev-web-db bash
psql -U postgres -d devbank -f /init.sql
```

</br>

### Usando Docker Compose (faça isso caso queira apenas ver a aplicação)

#### Pré-requisitos

Antes de começar, certifique-se de que você possui:

- [Docker](https://www.docker.com/) instalado e em execução

#### 1. Configurar variáveis de ambiente:

Copie o arquivo de exemplo `.env.example` para o `.env` na raiz do projeto e preencha as variáveis necessárias:
```bash
cp .env.example .env
# Edite o arquivo .env com os valores apropriados
```

#### 2. Subir os containers:

```bash
docker compose up
```

#### 3. Acessar a aplicação:

Abra no navegador: http://localhost:8080

---

## 👨‍💻 Desenvolvedores

- Luiz Felyppe Nunes dos Santos
- Mayara Frazão Guaraciaba de Lima
- Thiago Pereira Araujo
- Rodrigo Dias
- Gabriel
