# Fret.io

Fret.io é uma plataforma backend baseada em **arquitetura de microserviços** para gerenciamento de operações de frete, conectando empresas embarcadoras a motoristas freelas em um modelo semelhante ao Uber.

O sistema foi projetado com foco em **escalabilidade, desacoplamento e comunicação assíncrona**, utilizando mensageria para integração entre serviços.

Atualmente, o projeto encontra-se em fase de **implementação dos microserviços**, com infraestrutura provisionada, arquitetura e regras de negócio já definidas.

---

## 🚧 Status do Projeto

Em desenvolvimento

Atualmente, o projeto está na fase de evolução do API Gateway e implementação de funcionalidades centrais da plataforma.

### Etapa atual:

- Setup incial da API Gateway
- Configuração de rotas para os microserviços
- Filtro de autenticação JWT no Gateway
- Configuração de CORS global
- Rate limiting por IP
- Logging e tracing de requisições

---

## 🏗️ Arquitetura

O sistema é composto por **6 microserviços independentes**, cada um com seu próprio banco de dados PostgreSQL.

- Api Gateway
- Auth Service
- Company Service
- Driver Service
- Cargo Service
- Matching + Notification Service

**Princípios adotados:**

- Banco por serviço (isolamento total)
- Comunicação assíncrona via RabbitMQ
- Referências entre serviços via UUID (sem FK física)
- Arquitetura orientada a eventos (Event-Driven)

Nenhum serviço acessa diretamente o banco de outro.

---

## 🌐 API Gateway

A `api-gateway` atua como ponto de entrada único da aplicação, sendo responsável por rotear as requisições para os serviços internos.

### Porta

* `8080`

### Exemplo de rotas

| Rota          | Serviço        |
| ------------- | -------------- |
| `/auth/**`    | auth-service   |
| `/drivers/**` | driver-service |
| `/cargo/**`   | cargo-service  |

---

## 📡 Mensageria

A comunicação entre os serviços é feita via RabbitMQ, seguindo o padrão **Event-Driven Architecture**.

Exemplos de eventos definidos:

- user.registered
- driver.registered
- carga.publicada
- match.confirmado

Os serviços publicam e consomem eventos de forma desacoplada, garantindo escalabilidade e resiliência.

---

## 📚 Documentação

A documentação completa do projeto está disponível em:

docs/
└── documentacao-fretio.pdf 

---

## 🚀 Rodando o Projeto

### Pré-requisitos

- [Docker](https://www.docker.com/) instalado (Docker Compose já incluído) 

### Subindo o ambiente local

Clone o repositório:
```bash
git clone https://github.com/Moyses-Zerbieti/fret.io.git
cd fret.io
```

Copie o arquivo de variáveis de ambiente e preencha com os valores:
```bash
cp .env-template .env
```

Suba os serviços de infraestrutura:
```bash
docker compose up -d
```

### 🧩 Serviços disponíveis

| Serviço | URL | Credenciais |
|---|---|---|
| PostgreSQL | `localhost:5433` | definidas no `.env` |
| RabbitMQ (AMQP) | `localhost:5672` | definidas no `.env` |
| RabbitMQ (Painel) | `localhost:15672` | definidas no `.env` |

### 📊 RabbitMQ

Acesse `http://localhost:15672` no navegador e faça login com as credenciais definidas no `.env`.

No painel você pode:
- Visualizar exchanges e queues criadas
- Publicar mensagens de teste
- Monitorar consumers ativos
- Inspecionar DLQs

### ▶️ Rodando um serviço localmente

Entre na pasta do serviço desejado, copie o `.env-template` e preencha:
```bash
cd auth-service
cp .env-template .env
```

Rode o serviço:
```bash
./mvnw spring-boot:run
```
---

### 🗃️ Migrations

As migrations são gerenciadas pelo **Flyway** e executadas automaticamente ao subir cada serviço.

Os arquivos ficam em `src/main/resources/db/migration/` de cada serviço, nomeados no padrão `V{versão}__{descrição}.sql`.

#### auth-service
| Versão | Descrição |
|--------|-----------|
| V1 | Criação da tabela `users` |
| V2 | Criação da tabela `user_roles` |
| V3 | Criação da tabela `refresh_tokens` |
| V4 | Criação da tabela `password_resets` |

> Para habilitar o Flyway no serviço, adicione as dependências no `pom.xml`:
> 
> ```xml
> <dependency>
>     <groupId>org.flywaydb</groupId>
>     <artifactId>flyway-core</artifactId>
> </dependency>
> <dependency>
>     <groupId>org.flywaydb</groupId>
>     <artifactId>flyway-database-postgresql</artifactId>
> </dependency>
> ```
