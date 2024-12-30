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
    - Inicializar o serviço do **PostgreSQL** (porta padrão: `5432`).
    - Inicializar a instância do **Keycloak** (porta padrão: `8081`).

### **3. Configurando o Keycloak**
Após o Keycloak estar em execução:
1. **Acesse o painel de administração Keycloak**:
    - URL: [http://localhost:8081]()
    - Credenciais padrão:
        - **Username:** `admin`
        - **Password:** `admin`

2. **Criar um Realm**:
    - No painel, clique em "Add Realm" e crie um novo `Realm`, por exemplo, `Pedidos`.

3. **Criar um Cliente**:
    - Dentro do `Realm`, clique em "Clients" → "Create".
    - Configure o cliente:
        - `Client ID`: `pedidos-api`
        - `Access Type`: `confidential`

4. **Criar Roles**:
    - Dentro do `Realm`, clique em "Roles" → "Add Role".
    - Adicione as roles conforme necessidade, por exemplo:
        - `ROLE_CLIENTE_EXTERNO_UM`
        - `ROLE_CLIENTE_EXTERNO_DOIS`

5. **Criação de Usuários** (opcional):
    - No menu "Users", crie usuários que poderão acessar a API.
    - Atribua as roles criadas ao usuário.

### **4. Obtendo o Token de Acesso**
Para autenticar-se na API utilizando o Keycloak, siga os passos:
1. Execute a seguinte requisição para obter o token:
``` bash
   curl -X POST "http://localhost:8081/realms/Pedidos/protocol/openid-connect/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "username=<SEU_USUARIO>" \
     -d "password=<SUA_SENHA>" \
     -d "client_id=pedidos-api" \
     -d "grant_type=password"
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
### **1. Criar Pedido**:
**POST:** `/pedidos`
**Body:**
``` json
{
  "cpfCliente": "12345678909",
  "itens": [{"produtoId": 1, "quantidade": 2}]
}
```
### **2. Obter Pedidos Paginados**:
**GET:** `/pedidos?page=0&size=10`
### **3. Obter Pedido por ID**:
**GET:** `/pedidos/1`
### **4. Adicionar Produto ao Pedido**:
**POST:** `/pedidos/{id}/adicionarProduto`
**Body:**
``` json
{
  "produtoId": 2,
  "quantidade": 1
}
```
### **5. Cancelar Pedido**:
**POST:** `/pedidos/{id}/cancelar`
