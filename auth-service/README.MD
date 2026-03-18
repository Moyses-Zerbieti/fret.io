# auth-service

Microserviço responsável por [autenticação e autorização de usuários] na plataforma Fret.io.

## Banco de dados

- **Database**: `auth_db`
- **Usuário**: `auth_user`

## Variáveis de ambiente

Copie o `.env-template` e preencha com os valores reais:
```bash
cp .env.template .env
```

| Variável | Descrição |
|---|---|
| `DB_URL` | URL de conexão com o PostgreSQL |
| `DB_USER` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `RABBITMQ_HOST` | Host do RabbitMQ |
| `RABBITMQ_PORT` | Porta do RabbitMQ |
| `RABBITMQ_USER` | Usuário do RabbitMQ |
| `RABBITMQ_PASSWORD` | Senha do RabbitMQ |
| `JWT_SECRET` | Chave secreta para geração de tokens JWT |
| `JWT_EXPIRATION` | Tempo de expiração do token em ms |

## Como rodar localmente

Certifique-se que o ambiente está de pé:
```bash
cd ..
docker compose up -d
```

Rode o serviço:
```bash
./mvnw spring-boot:run
```