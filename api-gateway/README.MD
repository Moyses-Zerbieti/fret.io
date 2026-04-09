# api-gateway

Serviço responsável por atuar como ponto de entrada único das requisições externas da plataforma Fret.io, roteando chamadas para os microserviços internos.

## Arquitetura

O API Gateway centraliza o acesso aos serviços da plataforma, permitindo:

* Roteamento de requisições por path
* Abstração dos serviços internos
* Ponto único para aplicação de segurança
* Facilidade de versionamento de APIs

Fluxo básico:

Client → API Gateway → Microserviços

---

## Porta

* **Porta**: `8080`

---

## Rotas

| Rota            | Serviço          | Porta |
| --------------- | ---------------- | ----- |
| `/auth/**`      | auth-service     | 8081  |
| `/companies/**` | company-service  | 8082  |
| `/drivers/**`   | driver-service   | 8083  |
| `/cargo/**`     | cargo-service    | 8084  |
| `/matching/**`  | matching-service | 8085  |

---

## Health Check

* `GET /actuator/health`

Resposta esperada:

```json
{
  "status": "UP"
}
```

---

## Como rodar localmente

Certifique-se que os serviços estão de pé:

```bash
 cd ..
 docker compose up -d
```

Rode o gateway:

```bash
 ./mvnw spring-boot:run
```
---