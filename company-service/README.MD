# company-service

Microserviço responsável pelo cadastro e gerenciamento de empresas embarcadoras na plataforma Fret.io.

## Banco de dados

- **Database**: `companies_db`
- **Usuário**: `companies_user`

## Variáveis de ambiente

Copie o `.env-template` e preencha com os valores reais:

```bash
cp .env-template .env
```

| Variável          | Descrição                        |
|-------------------|----------------------------------|
| `DB_URL`          | URL de conexão com o PostgreSQL  |
| `DB_USER`         | Usuário do banco                 |
| `DB_PASSWORD`     | Senha do banco                   |
| `RABBITMQ_HOST`   | Host do RabbitMQ                 |
| `RABBITMQ_PORT`   | Porta do RabbitMQ                |
| `RABBITMQ_USER`   | Usuário do RabbitMQ              |
| `RABBITMQ_PASSWORD` | Senha do RabbitMQ              |

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