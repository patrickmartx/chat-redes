# chat-redes

Este é um sistema desenvolvido para facilitar a comunicação entre alunos e professores, permitindo que alunos cadastrem suas dúvidas, anexem imagens e iniciem um chat em tempo real com o professor para obter assistência.

## Funcionalidades Principais

### Aluno:
* **Cadastro de Dúvidas:**
    * Informar nome, turma e descrição detalhada da dúvida.
    * Anexar uma imagem para fornecer contexto visual.
    * Submeter a dúvida para o professor.
* **Chat em Tempo Real:**
    * Após submeter uma dúvida, um chat é criado para discussão com o professor.
    * Participar de conversas em tempo real sobre a dúvida específica.
* **Acessar Chat Existente:**
    * Opção na tela de cadastro de dúvida para inserir o ID de uma dúvida/chat já existente e ser redirecionado diretamente para a conversa.

### Professor:
* **Autenticação:**
    * Login com email e senha para acessar as funcionalidades do professor.
* **Visualização de Dúvidas:**
    * Acessar uma lista com todas as dúvidas enviadas pelos alunos.
    * Visualizar os detalhes completos de cada dúvida, incluindo a descrição e a imagem anexada.
* **Chat em Tempo Real:**
    * Iniciar ou participar de um chat com o aluno referente a uma dúvida específica.
    * Visualizar o histórico de mensagens da conversa.

## Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework Backend:** Spring Boot
    * Spring Web
    * Spring Data JPA
    * Spring WebSocket
* **Frontend:**
    * Thymeleaf
    * Bootstrap 5
    * JavaScript (Com SockJS e Stomp.js para o cliente WebSocket)
* **Banco de Dados:** H2 Database
* **Build Tool:** Maven
* **Deployment:** Docker e Docker Compose

## Pré-requisitos

* JDK 21 ou superior
* Maven 3.8.x ou superior (ou Gradle compatível)
* Docker
* Docker Compose

## Como Executar (Localmente)

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/patrickmartx/chat-redes.git
    cd chat-redes
    ```

2.  **Execute a aplicação (usando Maven):**
    ```bash
    mvn spring-boot:run
    ```
    Ou execute a classe principal `SistemaDuvidasApplication.java` a partir da sua IDE.

3.  **Acesse a aplicação:**
    * **Formulário de Dúvidas (Aluno):** `http://localhost:8080/duvidas/nova`
    * **Console H2:** `http://localhost:8080/h2-console`
        * **JDBC URL:** `jdbc:h2:file:./database/sistemaduvidasdb`
        * **User Name:** `sa`
        * **Password:** `password`
    * (As rotas do professor, como a lista de dúvidas, estarão disponíveis após a implementação do login).

## Como Executar (Com Docker)

1.  **Construa a aplicação e a imagem Docker:**
    Primeiro, empacote a aplicação (ex: se usando Maven):
    ```bash
    mvn clean package
    ```
    Em seguida, use o Docker Compose para construir a imagem e iniciar o contêiner:
    ```bash
    docker-compose up --build
    ```

2.  **Acesse a aplicação:**
    * `http://localhost:8080/duvidas/nova`

    Os dados do H2 e as imagens enviadas serão persistidos em volumes definidos no `docker-compose.yml` (nas pastas `./database` e `./uploads/images` na raiz do projeto, respectivamente).

## Entidades Principais

* **`Professor`**: Representa um professor no sistema.
    * `id` (Long): Identificador único.
    * `nome` (String): Nome do professor.
    * `email` (String): Email para login (único).
    * `senha` (String): Senha.
* **`Duvida`**: Representa uma dúvida submetida por um aluno.
    * `id` (Long): Identificador único.
    * `nomeAluno` (String): Nome do aluno que submeteu.
    * `turma` (String): Turma do aluno.
    * `descricao` (String): Texto da dúvida.
    * `dadosImagem` (byte[]): Bytes da imagem anexada.
    * `dataCriacao` (LocalDateTime): Data e hora da submissão.
* **`ChatMessage`**: Representa uma mensagem no chat.
    * `id` (Long): Identificador único.
    * `duvida` (Duvida): A dúvida à qual esta mensagem pertence.
    * `remetente` (String): Quem enviou a mensagem (ex: nome do aluno, nome do professor).
    * `conteudo` (String): O texto da mensagem.
    * `dadosImagem` (byte[]): Bytes da imagem anexada.
    * `timestamp` (LocalDateTime): Data e hora do envio da mensagem.
