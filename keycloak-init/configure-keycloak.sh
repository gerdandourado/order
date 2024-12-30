#!/bin/bash
set -e

echo "criando configs no keycloak!"

# Variáveis do Admin do Keycloak
KEYCLOAK_URL="http://localhost:9090"
KEYCLOAK_USER="admin"
KEYCLOAK_PASSWORD="123456"
REALM_NAME="avaliacao"
CLIENT_ID="cliente-externo-um"
CLIENT_PROTOCOL="openid-connect"
ACCESS_TYPE="confidential"
ROLE_NAME="ROLE_CLIENTE_EXTERNO_UM"

# Obter o token de acesso do Admin
ACCESS_TOKEN=$(http --form POST "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" \
  username="$KEYCLOAK_USER" \
  password="$KEYCLOAK_PASSWORD" \
  grant_type='password' \
  client_id='admin-cli' | python3 -c "import sys, json; print(json.load(sys.stdin)['access_token'])")

# Criar o realm
http POST "$KEYCLOAK_URL/admin/realms" \
  "Authorization: Bearer $ACCESS_TOKEN" \
  Content-Type:application/json \
  realm="$REALM_NAME" enabled:=true

# Criar o cliente dentro do realm "avaliacao"
CLIENT_SECRET=$(python3 -c "import os; print(os.urandom(16).hex())")
http POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients" \
  "Authorization: Bearer $ACCESS_TOKEN" \
  Content-Type:application/json \
  clientId="$CLIENT_ID" \
  protocol="$CLIENT_PROTOCOL" \
  publicClient:=false \
  secret="$CLIENT_SECRET" \
  serviceAccountsEnabled:=true \
  authorizationServicesEnabled:=true \
  directAccessGrantsEnabled:=false \
  standardFlowEnabled:=false

# Obter o ID do cliente recém-criado
CLIENT_UUID=$(http GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients" \
  "Authorization: Bearer $ACCESS_TOKEN" | python3 -c "import sys, json; clients = json.load(sys.stdin); print(next(c['id'] for c in clients if c['clientId'] == '$CLIENT_ID'))")

# Criar a role no realm "avaliacao"
http POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" \
  "Authorization: Bearer $ACCESS_TOKEN" \
  Content-Type:application/json \
  name="$ROLE_NAME"

# Obter o ID da role recém-criada
ROLE_ID=$(http GET "$KEYCLOAK_URL/admin/realms/$REALM_NAME/roles" \
  "Authorization: Bearer $ACCESS_TOKEN" | python3 -c "import sys, json; roles = json.load(sys.stdin); print(next(r['id'] for r in roles if r['name'] == '$ROLE_NAME'))")

# Atribuir a role ao cliente
http POST "$KEYCLOAK_URL/admin/realms/$REALM_NAME/clients/$CLIENT_UUID/roles" \
  "Authorization: Bearer $ACCESS_TOKEN" \
  Content-Type:application/json \
  "[{\"id\": \"$ROLE_ID\", \"name\": \"$ROLE_NAME\"}]"