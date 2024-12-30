CREATE TABLE produto (
                         id BIGINT PRIMARY KEY,
                         descricao VARCHAR(255) NOT NULL
);

CREATE TABLE pedido (
                        id BIGINT PRIMARY KEY,
                        cpf_cliente VARCHAR(11) NOT NULL,
                        data DATE NOT NULL,
                        situacao VARCHAR(50) NOT NULL
);

CREATE TABLE item_pedido (
                             id BIGSERIAL PRIMARY KEY,
                             id_produto BIGINT NOT NULL,
                             quantidade DECIMAL(19, 6),
                             valor_unitario DECIMAL(19, 6),
                             id_pedido BIGINT NOT NULL,
                             CONSTRAINT fk_item_pedido_produto FOREIGN KEY (id_produto) REFERENCES produto(id),
                             CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id)
);