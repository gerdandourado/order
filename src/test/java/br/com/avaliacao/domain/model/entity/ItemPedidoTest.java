package br.com.avaliacao.domain.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemPedidoTest {

    @Test
    void dadoValorUnitarioEQuantidadeValidos_quandoGetValorTotal_entaoRetornaValorTotal() {
        ItemPedido itemPedido = ItemPedido.builder()
                .valorUnitario(new BigDecimal("10.50"))
                .quantidade(new BigDecimal("2"))
                .build();

        BigDecimal resultado = itemPedido.getValorTotal();

        assertEquals(new BigDecimal("21.00"), resultado);
    }

    @Test
    void dadoValorUnitarioNuloEQuantidadeValida_quandoGetValorTotal_entaoRetornaZero() {
        ItemPedido itemPedido = ItemPedido.builder()
                .valorUnitario(null)
                .quantidade(new BigDecimal("2"))
                .build();

        BigDecimal resultado = itemPedido.getValorTotal();

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    void dadoValorUnitarioEQuantidadeNulos_quandoGetValorTotal_entaoRetornaZero() {
        ItemPedido itemPedido = ItemPedido.builder()
                .valorUnitario(null)
                .quantidade(null)
                .build();

        BigDecimal resultado = itemPedido.getValorTotal();

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    void dadoValorUnitarioValidoEQuantidadeNula_quandoGetValorTotal_entaoRetornaZero() {
        ItemPedido itemPedido = ItemPedido.builder()
                .valorUnitario(new BigDecimal("10.50"))
                .quantidade(null)
                .build();

        BigDecimal resultado = itemPedido.getValorTotal();

        assertEquals(BigDecimal.ZERO, resultado);
    }
}