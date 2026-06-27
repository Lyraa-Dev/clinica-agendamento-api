# Clínica - API de Agendamentos

API REST para controle de agendamentos de consultas de uma clínica, acompanhada de uma interface web em Angular para consumo. Permite cadastrar pacientes e profissionais, criar agendamentos aplicando regras de negócio, listar com filtros e cancelar mantendo o histórico.

Projeto desenvolvido como teste técnico para a vaga de Desenvolvedor(a) Júnior.

---

## Índice

- [Tecnologias](#tecnologias)
- [Estrutura do repositório](#estrutura-do-repositório)
- [Como executar o back-end](#como-executar-o-back-end)
- [Como executar o front-end](#como-executar-o-front-end)
- [Dados iniciais (seed)](#dados-iniciais-seed)
- [Endpoints da API](#endpoints-da-api)
- [Regras de negócio](#regras-de-negócio)
- [Roteiro de testes manuais](#roteiro-de-testes-manuais)
- [Testes automatizados](#testes-automatizados)
- [Banco de dados e compatibilidade Oracle](#banco-de-dados-e-compatibilidade-oracle)
- [Solução de problemas](#solução-de-problemas)

---

## Tecnologias

**Back-end**
- Java 21
- Spring Boot 3.4.1 (Spring Web, Spring Data JPA, Bean Validation)
- H2 Database em modo de compatibilidade Oracle
- SpringDoc OpenAPI (Swagger / documentação interativa)
- JUnit 5 e Mockito (testes automatizados)
- Maven

**Front-end**
- Angular 21 (standalone components e signals)
- TypeScript
- SCSS

---

## Estrutura do repositório

```
clinica-agendamento-api/
├── src/                  # Código do back-end (Java / Spring Boot)
│   ├── main/java/com/clinica/agendamento/
│   │   ├── controller/   # Endpoints REST
│   │   ├── service/      # Regras de negócio
│   │   ├── repository/   # Acesso ao banco (Spring Data JPA)
│   │   ├── model/        # Entidades JPA
│   │   ├── dto/          # Objetos de entrada e saída da API
│   │   ├── exception/    # Exceções e tratamento global de erros
│   │   └── config/       # Configuração de CORS
│   └── main/resources/
│       ├── application.properties
│       └── data.sql      # Carga de dados inicial
├── src/test/             # Testes automatizados
├── pom.xml               # Dependências do back-end
├── frontend/             # Aplicação Angular (interface web)
├── README.md
└── DECISOES.md           # Decisões técnicas e justificativas
```

O back-end segue uma arquitetura em camadas, com responsabilidades bem separadas:

```
controller  ->  recebe as requisições HTTP e devolve as respostas
service     ->  concentra as regras de negócio
repository  ->  acessa o banco de dados
model       ->  entidades mapeadas para as tabelas
dto         ->  contratos de entrada e saída (desacoplados das entidades)
```

---

## Como executar o back-end

**Pré-requisitos:** Java 21 instalado. O Maven é opcional, pois o projeto inclui o Maven Wrapper (`mvnw`).

Na raiz do projeto, execute:

```bash
# usando o Maven Wrapper (não exige Maven instalado)
./mvnw spring-boot:run

# no Windows (PowerShell ou CMD)
mvnw spring-boot:run

# ou, se preferir o Maven instalado globalmente
mvn spring-boot:run
```

A API sobe em **http://localhost:8080**.

> Na primeira execução, o Maven baixa as dependências — pode levar alguns minutos.

### Documentação interativa (Swagger)

Com a aplicação no ar, acesse:

```
http://localhost:8080/swagger-ui.html
```

Pelo Swagger é possível testar todos os endpoints diretamente pelo navegador, sem precisar de ferramentas externas.

### Console do banco (H2)

```
http://localhost:8080/h2-console
```

No campo **JDBC URL**, informe: `jdbc:h2:mem:clinicadb` — usuário `sa`, senha em branco.

---

## Como executar o front-end

**Pré-requisitos:** Node.js e Angular CLI instalados. O back-end precisa estar rodando.

```bash
cd frontend
npm install      # apenas na primeira vez
ng serve
```

Acesse **http://localhost:4200**.

A interface consome a API em `http://localhost:8080`. O CORS já está configurado no back-end para permitir essa comunicação durante o desenvolvimento.

---

## Dados iniciais (seed)

Como o banco roda em memória e é reiniciado a cada execução, o arquivo `src/main/resources/data.sql` popula automaticamente os dados abaixo **toda vez que a aplicação sobe**. Assim, o usuário já encontra registros prontos para criar agendamentos imediatamente.

**Profissionais pré-cadastrados:**

| ID | Nome               | Especialidade  | Registro      |
|----|--------------------|----------------|---------------|
| 1  | Dr. Carlos Andrade | Cardiologia    | CRM-PE 12345  |
| 2  | Dra. Beatriz Lima  | Dermatologia   | CRM-PE 67890  |
| 3  | Dr. Paulo Mendes   | Ortopedia      | CRM-PE 24680  |
| 4  | Dra. Renata Souza  | Pediatria      | CRM-PE 13579  |
| 5  | Dr. Felipe Costa   | Clínica Geral  | CRM-PE 11223  |

**Pacientes pré-cadastrados:**

| ID | Nome           | CPF          |
|----|----------------|--------------|
| 1  | Maria Silva    | 12345678901  |
| 2  | João Pereira   | 98765432100  |
| 3  | Ana Costa      | 11122233344  |
| 4  | Pedro Oliveira | 55566677788  |
| 5  | Juliana Santos | 99988877766  |
| 6  | Lucas Almeida  | 44455566677  |

> **Atenção:** os CPFs e registros acima já existem no banco. Ao testar o cadastro, use valores **diferentes** destes, pois a API bloqueia CPF e registro duplicados (ver regras de negócio).

---

## Endpoints da API

### Pacientes

| Método | Rota          | Descrição                |
|--------|---------------|--------------------------|
| POST   | `/pacientes`  | Cadastra um paciente     |
| GET    | `/pacientes`  | Lista todos os pacientes |

Exemplo de corpo (POST) — exemplo de dados que **não** colidem com o seed para praticidade na execução:

```json
{
  "nome": "Carla Nogueira",
  "cpf": "70011122233",
  "telefone": "81988776655"
}
```

### Profissionais

| Método | Rota             | Descrição                    |
|--------|------------------|------------------------------|
| POST   | `/profissionais` | Cadastra um profissional     |
| GET    | `/profissionais` | Lista todos os profissionais |

Exemplo de corpo (POST):

```json
{
  "nome": "Dra. Helena Rocha",
  "especialidade": "Neurologia",
  "registro": "CRM-PE 90001"
}
```

### Agendamentos

| Método | Rota                              | Descrição                        |
|--------|-----------------------------------|----------------------------------|
| POST   | `/agendamentos`                   | Cria um agendamento              |
| GET    | `/agendamentos`                   | Lista agendamentos (com filtros) |
| PATCH  | `/agendamentos/{id}/cancelamento` | Cancela um agendamento           |

Exemplo de corpo (POST) — referencia paciente e profissional já existentes pelo ID:

```json
{
  "pacienteId": 1,
  "profissionalId": 1,
  "dataHora": "2026-08-15T14:30:00",
  "tipoAtendimento": "Consulta de rotina"
}
```

O campo `dataHora` segue o formato ISO `AAAA-MM-DDTHH:MM:SS`.

A listagem aceita filtros opcionais por query string, em qualquer combinação:

```
GET /agendamentos?pacienteId=1
GET /agendamentos?profissionalId=2&status=AGENDADO
GET /agendamentos?status=CANCELADO
```

Exemplo de corpo para cancelamento (PATCH):

```json
{
  "motivo": "Paciente solicitou remarcação"
}
```

---

## Regras de negócio

- **CPF único:** não é permitido cadastrar dois pacientes com o mesmo CPF (não se trata de uma regra do desafio, porém decidi inserir como uma boa pratica).
- **Registro único:** não é permitido cadastrar dois profissionais com o mesmo registro (CRM/CRO) (não se trata de uma regra do desafio, porém decidi inserir como uma boa pratica).
- **Horário do profissional:** um profissional não pode ter dois agendamentos no mesmo horário. Agendamentos cancelados liberam o horário.
- **Sem data no passado:** não é permitido criar agendamento com data/hora anterior ao momento atual (com tolerância de 1 minuto para o horário corrente).
- **Cancelamento com motivo:** o cancelamento exige um motivo obrigatório.
- **Histórico preservado:** ao cancelar, o status muda para `CANCELADO` e o registro é mantido (não é excluído).
- **Filtros:** a listagem de agendamentos pode ser filtrada por paciente, profissional e/ou status.

---

## Roteiro de testes manuais

Sequência sugerida para validar as regras pelo Swagger ou pela interface:

1. **Listar dados do seed** — `GET /pacientes` e `GET /profissionais` devem retornar os registros pré-cadastrados.
2. **Criar um agendamento válido** — `POST /agendamentos` com `pacienteId` e `profissionalId` existentes e uma data futura. Retorna `201` com status `AGENDADO`.
3. **Conflito de horário** — repita o POST com o mesmo `profissionalId` e a mesma `dataHora`. Retorna `400` informando que o horário está ocupado.
4. **Data no passado** — crie um agendamento com `dataHora` de uma data anterior. Retorna `400`.
5. **Recurso inexistente** — crie um agendamento com `pacienteId` inexistente (ex.: `999`). Retorna `404`.
6. **CPF duplicado** — tente cadastrar um paciente com um CPF já existente (ex.: `12345678901`). Retorna `400`.
7. **Cancelar** — `PATCH /agendamentos/{id}/cancelamento` com um motivo. O status muda para `CANCELADO` e o registro continua na listagem.
8. **Horário liberado** — após cancelar, crie um novo agendamento no mesmo horário do que foi cancelado. Deve ser aceito (`201`), provando que o cancelamento libera a vaga.

---

## Testes automatizados

Para rodar os testes:

```bash
./mvnw test      # ou: mvn test
```

Os testes cobrem as regras de negócio do agendamento — criação válida, conflito de horário, data no passado, recurso inexistente e cancelamento — utilizando JUnit 5 e Mockito (com os repositórios mockados, sem necessidade de banco).

---

## Banco de dados e compatibilidade Oracle

O projeto usa **H2 em memória**, configurado em **modo de compatibilidade Oracle** (`MODE=Oracle` na URL de conexão). Essa decisão tem dois objetivos:

1. **Facilitar a execução:** quem avalia roda o projeto sem instalar nenhum banco — sobe com um comando.
2. **Demonstrar compatibilidade Oracle:** com `MODE=Oracle`, o H2 interpreta a sintaxe SQL como o Oracle faria, atendendo ao diferencial pedido sem exigir um Oracle real.

Por ser em memória, o banco é reiniciado a cada execução e repovoado pelo `data.sql`.

### Apontando para um Oracle real

Para usar um Oracle de verdade, bastam dois ajustes:

1. No `pom.xml`, descomentar a dependência do driver Oracle (`ojdbc11`).
2. No `application.properties`, substituir a URL do H2 pela do Oracle, por exemplo:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

Como o mapeamento JPA é independente do banco, nenhuma mudança no código Java é necessária.

---

## Solução de problemas

- **A porta 8080 já está em uso:** encerre o processo que a ocupa ou altere a porta no `application.properties` com `server.port=8081`.
- **O front-end não carrega os dados:** confirme que o back-end está rodando em `http://localhost:8080`. As tabelas da interface mostram uma mensagem de erro quando a API está indisponível.
- **Erro de CPF ou registro duplicado ao testar cadastro:** use valores diferentes dos que já vêm no seed (ver seção "Dados iniciais").
- **O `data.sql` não carregou:** verifique se a propriedade `spring.jpa.defer-datasource-initialization=true` está presente no `application.properties` — ela garante que o seed rode após a criação das tabelas.