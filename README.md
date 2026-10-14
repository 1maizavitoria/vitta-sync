
# VittaSync

  

O VittaSync é uma plataforma de monitoramento de saúde onde paciente registra os sinais vitais, hábitos e sintomas, e os responsáveis (familiares, cuidadores, etc) e profissionais de saúde vinculados monitoram esses dados em gráficos, linha do tempo e relatórios em conjunto com o sistema, que proporciona indicadores e alertas, por e-mail e SMS, em diversos casos de anormalidades.

  

## Funcionalidades

  

-  **Contas e login:** cadastro de paciente, responsável e profissional de saúde, com login em duas etapas (código enviado por e-mail ou SMS) e redefinição de senha.

-  **Vínculos:** o paciente gera um código de convite e os responsáveis e profissionais de saúde entram com esse código para acompanhar os dados dele.

-  **Sinais vitais:** peso, frequência cardíaca, frequência respiratória, pressão arterial, temperatura e saturação de oxigênio do paciente.

-  **Hábitos:** horas de sono e minutos de exercício, com cálculo do índice de repouso do paciente.

-  **Diário de sintomas:** sintomas do paciente, com intensidade da dor de 1 a 10.

-  **Alertas:** pressão alterada, febre, saturação baixa, dor intensa, sono crítico, sedentarismo, entre outros. Aparecem como notificações no sistema e são enviados aos contatos de emergência.

-  **Estabilidade clínica:** índice que classifica cada fator e a saúde geral do paciente em saudável, moderado ou crítico.

-  **Linha de base:** faixa normal de cada sinal vital calculada a partir das medições do paciente após 14 dias seguidos, utilizada para definir e medir anormalidades.

-  **Dashboard e linha do tempo:** gráficos e histórico dos registros em períodos definidos.

-  **Metas de acompanhamento:** metas de peso, sono, exercício ou personalizadas, com progresso calculado e integrado automaticamente.

-  **Lembretes de medição:** lembrete enviado por email, SMS ou ambos, nos dias da semana e horários definidos pelo paciente, para notificar sobre a necessidade de registrar as medições.

-  **Documentos médicos:** envio, em PDF ou imagem, de documentos médicos por parte do profissional de saúde, com pré-visualização e download disponíveis também para pacientes e seus responsáveis.

-  **Relatórios:** exportação dos dados do paciente em PDF ou CSV, com personalização por período e tipo de dados.

  

## Tecnologias

  

- **Backend:** Java 21, Spring Boot 3.5, Spring Security + JWT, Spring Data JPA, MySQL 8, Swagger (springdoc), iText e JFreeChart (PDF), Twilio (SMS), Gmail SMTP (e-mail).

  

- **Frontend:** React 19, Vite, Material UI, Recharts, Axios.

  

- **Testes:** JUnit 5, Mockito, H2 e JaCoCo (backend), Vitest e Testing Library (frontend)

  

## Estrutura do projeto

  

O repositório tem as duas aplicações juntas:

  

```

vitta-sync/

├── VittaSyncAPI/ backend (Spring Boot)

│ └── src/main/java/br/com/vittasync/vittasync/

│ ├── Controller/ rotas da API

│ ├── Service/ regras de negócio

│ ├── Repository/ acesso ao banco

│ ├── Model/ tabelas do banco

│ ├── DTO/ dados de entrada e saída da API

│ ├── Security/ configuração do login com JWT

│ ├── Scheduler/ tarefas automáticas (lembretes e estabilidade)

│ ├── Exception/ erros tratados da API

│ └── Util/ funções de apoio e geração de PDF/CSV

├── components/ componentes visuais do frontend

├── pages/ telas do frontend

├── services/ chamadas do frontend para a API

├── routes/ rotas das telas

├── src/ entrada do frontend, tema e traduções

└── vittasync.sql script de criação do banco

```

  

## Necessário instalar:

  

-  **Java 21** (JDK)

-  **Maven 3.9** (ou superior)

-  **Node.js 22** (ou superior)

-  **MySQL 8**

  

## Configurando o ambiente

  

### 1. Baixar o projeto e instalar as dependências do frontend

  

```bash

git  clone <url-do-repositorio>

cd  vitta-sync

npm  install

```

  

### 2. Criar o banco de dados

  

Abra o arquivo `vittasync.sql` no MySQL Workbench e execute o script inteiro. Ele cria o banco `vittasync`, as tabelas, os índices e as procedures usadas pelo sistema (o script começa com `DROP DATABASE IF EXISTS vittasync`, então o banco anterior é apagado caso exista).

  

### 3. Configurar o backend

As configurações ficam em `VittaSyncAPI/src/main/resources/application.properties`:

  

-  **Banco de dados:**  `spring.datasource.username` e `spring.datasource.password` -> usuário e a senha do seu MySQL.

-  **SMS (Twilio):** as chaves não ficam no repositório, o arquivo usa variáveis de ambiente:

  

```properties

twilio.account.sid=${TWILIO_ACCOUNT_SID}

twilio.auth.token=${TWILIO_AUTH_TOKEN}

twilio.phone.number=${TWILIO_PHONE_NUMBER}

```

  

É possível criar as variáveis de ambiente no computador ou colocar as chaves direto no arquivo na máquina local.

  

## Rodar o projeto

  

Comandos a seguir executados na pasta raiz `vitta-sync`:

  

`npm run start-all` -> roda o projeto completo (backend + frontend) -> http://localhost:8080 (apenas backend via swagger) e http://localhost:5173 (frontend interativo)

`npm run back` -> apenas backend -> http://localhost:8080

`npm run dev` -> apenas frontend -> http://localhost:5173

  

Backend também pode ser iniciado direto pela pasta `VittaSyncAPI` com `mvn spring-boot:run`.

  

Frontend depende diretamente do backend estar rodando corretamente.

  

## Documentação da API (Swagger)

  

Com o backend rodando, acesse **http://localhost:8080/swagger**.

  

Estão todos os endpoints separados por grupo, com a descrição incluindo função, permissão, parâmetros e exemplos de dados.

  

## Testes

  

`npm run test-all` -> roda todos os testes unitários do sistema (backend + frontend) e copia o relatório de resultado para `reports/`.

`npm run test-back` -> roda os testes unitários do backend com perfil realista (MySQL) e copia o relatório de resultado para `reports/`.

`npm run test-front` -> roda os testes unitários do frontend e copia o relatório de resultado para `reports/`.

  

Para testes do backend utilizando perfil de testes (base de dados H2 local):

  

```bash

cd  VittaSyncAPI

mvn  clean  test  jacoco:report  "-Dspring.profiles.active=test"

```

  

O relatório de cobertura fica em `VittaSyncAPI/target/site/jacoco/index.html`.

  

Para testes do frontend utilizando perfil de testes :

  

```bash

npx  vitest  run

```

  

## Logs

  

Gravados pelo backend em `VittaSyncAPI/logs/vittasync.log`. O arquivo é dividido a cada 10 MB e são mantidos os últimos 7 dias.