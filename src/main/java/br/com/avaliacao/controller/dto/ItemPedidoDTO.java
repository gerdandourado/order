package br.com.avaliacao.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPedidoDTO(@NotNull @JsonAlias("id-produto") Long idProduto,
                            @NotNull BigDecimal quantidade,
                            @JsonAlias("valor-unitario") BigDecimal valorUnitario) {}
