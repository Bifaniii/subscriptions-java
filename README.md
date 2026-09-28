# Subscriptions API

![CI](https://github.com/Bifaniii/subscriptions-java/actions/workflows/ci.yml/badge.svg)

API REST para gerenciar planos de assinatura (BASIC, PREMIUM, VIP) de usuários autenticados com JWT. Cada usuário tem uma assinatura com validade de um mês a partir da contratação.

## Tecnologias

- Java 17 e Spring Boot 4
- Spring Web, Spring Data JPA e Bean Validation
- Spring Security com JWT stateless (jjwt)
- PostgreSQL 16
- Docker e Docker Compose
- JUnit 5, Mockito e MockMvc nos testes, com H2 em memória

## Endpoints

| Método | Rota | Descrição | Autenticação |
| :----- | :--- | :-------- | :----------- |
| POST | `/auth/register` | Cadastra usuário e retorna token | Não |
| POST | `/auth/login` | Autentica e retorna token | Não |
| GET | `/subscriptions` | Lista assinaturas | Bearer |
| GET | `/subscriptions/{id}` | Busca assinatura por id | Bearer |
| POST | `/subscriptions` | Contrata assinatura para o usuário logado | Bearer |
| PUT | `/subscriptions/{id}` | Atualiza tipo e preço | Bearer |
| PATCH | `/subscriptions/{id}` | Atualização parcial | Bearer |
| DELETE | `/subscriptions/{id}` | Remove assinatura | Bearer |

## Como rodar

Copie o arquivo de exemplo e preencha as variáveis:

```bash
cp .env.example .env
```

`JWT_SECRET` precisa ser uma chave em Base64 com pelo menos 32 bytes (`openssl rand -base64 32` gera uma).

Suba a API e o banco:

```bash
docker compose up --build
```

A API fica em `http://localhost:8080`.

## Testes

Testes unitários dos services (assinaturas, autenticação e JWT) com JUnit 5 e Mockito, e testes do controller com MockMvc. O teste de contexto usa H2, então não precisa de PostgreSQL:

```bash
./mvnw test
```

O GitHub Actions roda a suíte a cada push na `main` e em pull requests.

## Próximos passos

- Handler global de exceções (hoje "não encontrado" responde 500)
- DTO de saída sem expor a entidade `User`
- Regras de acesso por perfil (ADMIN/USER)
