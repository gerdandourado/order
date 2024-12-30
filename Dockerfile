## Etapa 1: Usar uma imagem base "builder" para instalar dependências
#FROM debian:bullseye-slim as builder
#
## Atualizar pacotes e instalar curl e jq
#RUN apt-get update && apt-get install -y httpie python3 \
#    && apt-get clean && rm -rf /var/lib/apt/lists/*
## Etapa 2: Imagem minimalista de Keycloak com as ferramentas copiadas
#FROM quay.io/keycloak/keycloak:latest
#
#USER root
##
### Copiar executáveis do builder
##COPY --from=builder /usr/bin/curl /usr/bin/curl
##COPY --from=builder /usr/bin/jq /usr/bin/jq
##
### Copiar bibliotecas compartilhadas necessárias
##COPY --from=builder /usr/lib/x86_64-linux-gnu/libcurl.so.4 /usr/lib/x86_64-linux-gnu/libcurl.so.4
##COPY --from=builder /usr/lib/x86_64-linux-gnu/libjq.so.1 /usr/lib/x86_64-linux-gnu/libjq.so.1
###COPY --from=builder /lib/x86_64-linux-gnu/libonig.so.5 /lib/x86_64-linux-gnu/libonig.so.5
##
### Garantir permissões corretas
##RUN chmod +x /usr/bin/curl /usr/bin/jq
#
## Tornar o script configurável
##RUN chmod +x /opt/keycloak-init/configure-keycloak.sh
#
#USER keycloak