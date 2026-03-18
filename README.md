# Fret.io

Fret.io é uma plataforma backend baseada em **arquitetura de microserviços** para gerenciamento de operações de frete, conectando empresas embarcadoras a motoristas freelas em um modelo semelhante ao Uber.

O sistema foi projetado com foco em **escalabilidade, desacoplamento e comunicação assíncrona**, utilizando mensageria para integração entre serviços.

Atualmente, o projeto encontra-se em fase de **estruturação da infraestrutura e implementação dos microserviços**, com arquitetura e regras de negócio já definidas.

---

## 🚧 Status do Projeto

Em desenvolvimento

Etapa atual:

- Configuração da infraestrutura com Docker
- Definição da arquitetura de microserviços
- Modelagem completa dos bancos PostgreSQL por serviço
- Definição dos eventos e integração via RabbitMQ

Próximos passos:

- Implementação do Auth Service
- Publicação e consumo de eventos
- Orquestração entre serviços

---

## 🏗️ Arquitetura

O sistema é composto por **5 microserviços independentes**, cada um com seu próprio banco de dados PostgreSQL.

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

## 📡 Mensageria

A comunicação entre os serviços é feita via RabbitMQ, seguindo o padrão **Event-Driven Architecture**.

Exemplos de eventos definidos:

- user.registered
- driver.registered
- carga.publicada
- match.confirmado

Os serviços publicam e consomem eventos de forma desacoplada, garantindo escalabilidade e resiliência.
