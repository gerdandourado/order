package br.com.avaliacao.domain.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PedidoTest {

    @Test
    void dadoItensValidos_quandoGetValorTotal_entaoRetornaSomaDosValoresTotais() {
        ItemPedido item1 = ItemPedido.builder()
                .valorUnitario(new BigDecimal("12.30"))
                .quantidade(new BigDecimal("3"))
                .build();
        ItemPedido item2 = ItemPedido.builder()
                .valorUnitario(new BigDecimal("7.50"))
                .quantidade(new BigDecimal("4"))
                .build();

        Pedido pedido = Pedido.builder().itens(List.of(item1, item2)).build();

        BigDecimal resultado = pedido.getValorTotal();

        assertEquals(new BigDecimal("66.90"), resultado);
    }

    @Test
    void dadoItensNulos_quandoGetValorTotal_entaoRetornaZero() {
        Pedido pedido = Pedido.builder().itens(null).build();

        BigDecimal resultado = pedido.getValorTotal();

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    void dadoItensComValorZero_quandoGetValorTotal_entaoRetornaZero() {
        ItemPedido item1 = ItemPedido.builder()
                .valorUnitario(BigDecimal.ZERO)
                .quantidade(new BigDecimal("10"))
                .build();
        ItemPedido item2 = ItemPedido.builder()
                .valorUnitario(new BigDecimal("0.00"))
                .quantidade(new BigDecimal("5"))
                .build();

        Pedido pedido = Pedido.builder().itens(List.of(item1, item2)).build();

        BigDecimal resultado = pedido.getValorTotal();

        assertEquals(new BigDecimal("0.00"), resultado);
    }
}