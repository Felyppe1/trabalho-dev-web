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

#### 1. Subir os containers:

```bash
docker compose up --build
```

#### 2. Inicializar o banco com script `init.sql`:

```bash
docker cp init.sql trabalho-dev-web-db:/init.sql
docker exec -it trabalho-dev-web-db bash
psql -U postgres -d devbank -f /init.sql
```

#### 3. Compilar o projeto (se necessário):

```bash
./build.sh
```

#### 4. Acessar a aplicação:

Abra no navegador: http://localhost:8080/trabalho-dev-web/home

---

## 👨‍💻 Desenvolvedores

- Luiz Felyppe Nunes dos Santos
- Mayara Frazão Guaraciaba de Lima
- Thiago
- Rodrigo Dias
- Gabriel