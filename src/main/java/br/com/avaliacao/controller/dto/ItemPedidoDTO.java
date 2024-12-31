package br.com.avaliacao.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemPedidoDTO(@NotNull @JsonAlias("id-produto") Long idProduto,
                            @JsonAlias("desc-produto") String descProduto,
                            @NotNull BigDecimal quantidade,
                            @JsonAlias("valor-unitario") BigDecimal valorUnitario) {}
