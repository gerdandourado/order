package br.com.avaliacao.controller.dto;

import br.com.avaliacao.domain.model.entity.Situacao;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDTO(
        @NotNull Long id,
        @JsonAlias("cpf-cliente")
        @NotNull @NotBlank String cpfCliente,
        @NotNull LocalDateTime data,
        @NotNull Situacao situacao,
        BigDecimal valorTotal,
        List<ItemPedidoDTO> itens) {
}
