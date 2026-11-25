# Gerenciamento de adoções

API REST em Spring Boot, organizada como projeto Maven. Para executá-la localmente, é necessário Java 21, Maven e Docker Desktop.

## Iniciar a API

Na raiz do projeto, inicie o PostgreSQL:

```powershell
docker compose up -d db
```

Depois, em outro terminal, inicie a aplicação:

```powershell
mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8080`. A documentação interativa fica em `http://localhost:8080/swagger-ui/index.html`.

O banco local usa a porta `5433` para não conflitar com um PostgreSQL já instalado na porta padrão. A aplicação cria e atualiza as tabelas automaticamente nesse banco. Para personalizar a conexão, defina `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.

Para parar o banco sem apagar os dados:

```powershell
docker compose stop db
```

## Paginação das listagens

`GET /ongs`, `/animais`, `/adotantes` e `/adocoes` aceitam paginação opcional:

- Sem parâmetros, devolvem um array com todos os registros (usado nos selects do cadastro de adoção).
- Com `page` (começa em 0) e/ou `size` (1 a 100, padrão 20), devolvem uma página ordenada por id:

```json
{ "content": [ ... ], "page": 0, "size": 20, "totalElements": 137, "totalPages": 7 }
```
