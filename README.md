# **Projeto de Gerenciamento de Pedidos**
Este projeto implementa uma API REST para o gerenciamento de pedidos utilizando o framework Spring Boot, com autenticação e autorização realizada através do Keycloak. Este documento fornece informações sobre os padrões e frameworks utilizados, bem como um guia de configuração e execução do projeto.
## **Padrões Utilizados**
1. **Arquitetura em Camadas (MVC)**:
    - **Controller**: Expõe os endpoints REST, realiza a validação das entradas e delega as operações à camada de serviço.
    - **Service**: Contém a lógica de negócios da aplicação (implementação das regras do domínio).
    - **Repository**: Realiza a interação com o banco de dados usando Spring Data JPA.

2. **DTO (Data Transfer Object)**:
    - Transfere dados entre as camadas de forma segura e padronizada, evitando a manipulação direta das entidades.

3. **Injeção de Dependências**:
    - Utiliza a funcionalidade do Spring para injetar dependências nas classes, promovendo um acoplamento fraco.

4. **Padrão de Conversão (Converter)**:
    - Objetos específicos (como entidades) são convertidos em DTOs e vice-versa, utilizando classes dedicadas (e.g., `PedidoConverter` e `ItemPedidoConverter`).

5. **Beans Validation**:
    - Utiliza as anotações da especificação Jakarta Bean Validation (e.g., `@NotNull`, `@Valid`) para validação de campos a nível de entidades e controladores.

6. **OpenAPI (Swagger)**:
    - Documentação automatizada dos endpoints REST, com suporte interativo diretamente na interface do Swagger UI para testes e aprendizado.

7. **Autenticação e Autorização**:
    - Configuração de segurança integrada ao Keycloak com controle baseado em roles.

## **Frameworks Utilizados**
1. **Spring Boot**:
    - Framework principal do projeto.
    - Módulos utilizados:
        - **Spring Web**: Implementação dos endpoints REST.
        - **Spring Security**: Integração com o Keycloak para autenticação e autorização.
        - **Spring Data JPA**: Persistência de dados de maneira simplificada.
        - **Spring Validation**: Usado para validação de dados.

2. **Keycloak**:
    - Sistema de gerenciamento de autenticação/autorização com tokens JWT baseados no protocolo OAuth2.

3. **Jakarta Validation**:
    - Implementação para validar as entradas no nível das entidades e controladores.

4. **Lombok**:
    - Reduz o boilerplate do código utilizando anotações como `@Getter`, `@Setter`, `@Builder`, etc.

5. **Banco de dados**:
    - **PostgreSQL** (com `docker-compose.yml`).
    
6. **OpenAPI e Swagger**:
    - Gerar documentação interativa dos endpoints.

## **Guia de Configuração e Uso**
### **1. Requisitos do Ambiente**
- **Java 17**:
    - Certifique-se de que o JDK 17 está instalado na sua máquina.
    - Caso não esteja, baixe o OpenJDK [aqui]().
    - Verifique a instalação:
``` bash
    java -version
```
- **Docker e Docker Compose**:
    - Instale o Docker na sua máquina (caso não esteja instalado):
        - [Instalação do Docker]().

    - Após instalação, confira se está funcionando:
``` bash
    docker --version
    docker-compose --version
```
### **2. Configurando o Banco e Keycloak com Docker Compose**
1. No diretório raiz do projeto, execute o seguinte comando para inicializar os serviços do banco de dados e do Keycloak configurados no `docker-compose.yml`:
``` bash
   docker-compose up -d
```
1. O comando acima permitirá:
    - Inicializar o serviço do **PostgreSQL** (porta padrão: `5434`).
    - Inicializar a instância do **Keycloak** (porta padrão: `9090`).

### **3. Configurando o Keycloak**
Após o Keycloak estar em execução:
1. **Acesse o painel de administração Keycloak**:
    - URL: [http://localhost:9090]()
    - Credenciais padrão:
        - **Username:** `admin`
        - **Password:** `123456`

2. **Criar um Realm**:
    - No painel, clique em "Add Realm" e crie um novo `Realm`, com nome: `ambev`.

3. **Criar um Cliente**:
    - Dentro do `Realm`, clique em "Clients" → "Create".
    - Configure o cliente:
      - `Client ID`: `cliente-externo-a`
      - `Access Type`: `confidential` ou `Client authentication`: `on`
      - `Authentication flow`: selecione `Service accounts roles `
      - Na aba `Credentials` é possível recuperar o `Client Secret`
    - Configure outro cliente:
      - `Client ID`: `cliente-externo-b`
      - `Access Type`: `confidential` ou `Client authentication`: `on`
      - `Authentication flow`: selecione `Service accounts roles `
      - Na aba `Credentials` é possível recuperar o `Client Secret`

4. **Criar Roles**:
    - Dentro do `Realm`, clique em "Roles" → "Add Role".
    - Adicione as roles conforme necessidade, por exemplo:
        - `ROLE_CLIENTE_EXTERNO_A`
        - `ROLE_CLIENTE_EXTERNO_B`        

5. **Associar Role ao client**
    - No menu "Client", acesse a aba "Service account roles"
    - Atribua as roles criadas aos clients
    - A role `ROLE_CLIENTE_EXTERNO_A` abribuir ao client `cliente-externo-a`
    - A role `ROLE_CLIENTE_EXTERNO_B` abribuir ao client `cliente-externo-b`

### **4. Obtendo o Token de Acesso**
Para autenticar-se na API utilizando o Keycloak, siga os passos:
1. Execute a seguinte requisição para obter o token:
``` bash
   curl -X POST "http://localhost:9090/realms/ambev/protocol/openid-connect/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "client_id=cliente-externo-a" \
     -d "client_secret=<DIGITE_A_SECRET_DO_CLIENT>" \
     -d "grant_type=client_credentials"
```
1. No retorno, você receberá um JSON com o **access token**:
``` json
   {
     "access_token": "SEU_TOKEN_AQUI",
     "expires_in": 300,
     "refresh_token": "...",
     ...
   }
```
1. Utilize o valor de `access_token` no header `Authorization` para consumir os endpoints da API.

### **5. Executando o Projeto**
1. Compile e inicie o projeto:
``` bash
   ./mvnw spring-boot:run
```
1. A API estará disponível em: `http://localhost:8081`.

## **Exemplos de Chamadas REST à Classe PedidoController**
### **1. `GET /pedidos` – Obter todos os pedidos**
- **Descrição**: Recupera uma lista de pedidos, com paginação e filtragem por data.
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_DOIS`.

**Exemplo de Requisição com data de corte:**
``` bash
curl -X GET "http://localhost:8081/order/pedidos?data-de-corte=2024-12-31T09:00:00&page=0&size=10&sortBy=id" \
     -H "Authorization: Bearer <TOKEN_JWT>"
```
**Exemplo de Requisição sem data de corte:**
``` bash
curl -X GET "http://localhost:8081/order/pedidos?page=0&size=10&sortBy=id" \
     -H "Authorization: Bearer <TOKEN_JWT>"
```

**Parâmetros:**
- `data-de-corte` (opcional): Data no formato `yyyy-MM-ddTHH:mm:ss` para filtrar pedidos.
- `page` (opcional): Índice da página.
- `size` (opcional): Tamanho da página.
- `sortBy` (opcional): Campo de ordenação.

### **2. `GET /pedidos/{id}` – Obter pedido por ID**
- **Descrição**: Recupera informações de um pedido específico.
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_UM`.

**Exemplo de Requisição:**
``` bash
curl -X GET "http://localhost:8081/order/pedidos/1" \
     -H "Authorization: Bearer <TOKEN_JWT>"
```
- Substitua `123` pelo ID do pedido.

### **3. `POST /pedidos` – Criar um novo pedido**
- **Descrição**: Cria um pedido com seus itens.
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_UM`.

**Exemplo de Requisição:**
``` bash
curl -X POST "http://localhost:8081/order/pedidos" \
     -H "Authorization: Bearer <TOKEN_JWT>" \
     -H "Content-Type: application/json" \
     -d '{
            "id": 1,
            "situacao": "CRIADO",
            "cpf-cliente": "02769113348",
            "data": "2024-12-31T12:00:00",
            "itens": [
               {
                  "id-produto": 2,
                  "quantidade": 2,
                  "valor-unitario": 3.2
               }
            ]
         }'
```
### **4. `POST /pedidos/{id}/adicionarProduto` – Adicionar produto ao pedido**
- **Descrição**: Adiciona um produto existente a um pedido identificado pelo ID.
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_UM`.

**Exemplo de Requisição:**
``` bash
curl -X POST "http://localhost:8081/order/pedidos/123/adicionarProduto" \
     -H "Authorization: Bearer <TOKEN_JWT>" \
     -H "Content-Type: application/json" \
     -d '{
           "id-produto": 1,
           "quantidade": 3,
           "valor-unitario": 3.5
         }'
```
- Substitua `123` pelo ID do pedido.

### **5. `POST /pedidos/{id}/removerProduto/{idProduto}` – Remover produto do pedido**
- **Descrição**: Remove um produto específico de um pedido.
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_UM`.

**Exemplo de Requisição:**
``` bash
curl -X POST "http://localhost:8081/order/pedidos/123/removerProduto/101" \
     -H "Authorization: Bearer <TOKEN_JWT>"
```
- Substitua `123` pelo ID do pedido e `101` pelo ID do produto a ser removido.

### **6. `POST /pedidos/{id}/cancelar` – Cancelar pedido**
- **Descrição**: Cancela um pedido, mudando seu status para "CANCELADO".
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_UM`.

**Exemplo de Requisição:**
``` bash
curl -X POST "http://localhost:8081/order/pedidos/123/cancelar" \
     -H "Authorization: Bearer <TOKEN_JWT>"
```
- Substitua `123` pelo ID do pedido.

### **7. `POST /pedidos/{id}/finalizar` – Finalizar pedido**
- **Descrição**: Finaliza um pedido, mudando seu status para "FINALIZADO".
- **Autorização**: Necessita de um token válido com a role `ROLE_CLIENTE_EXTERNO_UM`.

**Exemplo de Requisição:**
``` bash 
curl -X POST "http://localhost:8081/order/pedidos/123/finalizar" \
     -H "Authorization: Bearer <TOKEN_JWT>"
```
- Substitua `123` pelo ID do pedido.

### Observações Importantes:
1. **Autorização**:
   - Utilize um token JWT válido no cabeçalho `Authorization: Bearer <TOKEN_JWT>` conforme as roles exigidas em cada método.

2. **Corpo da requisição**:
   - Para métodos `POST`, envie os dados no formato JSON.
   - Certifique-se de validar os parâmetros exigidos (ex., IDs de pedidos ou produtos).

3. **Códigos de retorno esperados**:
   - `200 OK`: Operação bem-sucedida.
   - `201 CREATED`: Pedido ou produto criado com sucesso.
   - `403 FORBIDDEN`: Usuário não autorizado.
   - `404 NOT FOUND`: Pedido ou produto não encontrado.
   - `400 BAD REQUEST`: Dados inválidos.

