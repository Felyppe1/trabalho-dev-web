# 💸 DevBank

DevBank é um sistema bancário simples desenvolvido como parte do trabalho da disciplina **Desenvolvimento Web**. O projeto utiliza **Java com Servlets e JSP**, conectando-se a um banco de dados PostgreSQL para simular operações básicas bancárias, como login, visualização de extrato e movimentações financeiras.

## 🛠 Tecnologias utilizadas

- Java 17+
- Servlets & JSP
- Apache Tomcat
- PostgreSQL
- Docker

---

## 🚀 Como rodar o projeto

### ✅ Usando Docker (recomendado)

> Certifique-se de ter o Docker instalado antes de prosseguir.
> 

#### 1. Configurar variáveis de ambiente:

Copie o arquivo de exemplo `.env.example` para `.env` e preencha as variáveis necessárias:
```bash
cp .env.example .env
# Edite o arquivo .env com os valores apropriados
```

#### 2. Subir os containers:

```bash
docker compose up --build
```

#### 3. Inicializar o banco com script `init.sql`:

```bash
docker cp init.sql trabalho-dev-web-db:/init.sql
docker exec -it trabalho-dev-web-db bash
psql -U postgres -d devbank -f /init.sql
```

#### 4. Compilar o projeto (executar na raiz do host):

```bash
./build.sh
```

#### 5. Acessar a aplicação:

Abra no navegador: http://localhost:8080/trabalho-dev-web/home

---

## 👨‍💻 Desenvolvedores

- Luiz Felyppe Nunes dos Santos
- Mayara Frazão Guaraciaba de Lima
- Thiago Pereira Araujo
- Rodrigo Dias
- Gabriel