package br.com.avaliacao.domain.repository;

import br.com.avaliacao.domain.model.entity.ItemPedido;
import br.com.avaliacao.domain.model.entity.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemPedidoRepositoryTest {

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private ItemPedidoRepositoryTest itemPedidoRepositoryTest;

    @Test
    void dadoIdPedidoEIdProdutoValidos_quandoFindByPedido_IdAndProduto_Id_entaoRetornaItemPedido() {
        ItemPedido itemPedido = ItemPedido.builder().id(1L).build();
        when(itemPedidoRepository.findByPedido_IdAndProduto_Id(1L, 1L)).thenReturn(Optional.of(itemPedido));

        Optional<ItemPedido> resultado = itemPedidoRepository.findByPedido_IdAndProduto_Id(1L, 1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(itemPedidoRepository, times(1)).findByPedido_IdAndProduto_Id(1L, 1L);
    }

    @Test
    void dadoIdPedidoEIdProdutoInvalidos_quandoFindByPedido_IdAndProduto_Id_entaoRetornaVazio() {
        when(itemPedidoRepository.findByPedido_IdAndProduto_Id(1L, 2L)).thenReturn(Optional.empty());

        Optional<ItemPedido> resultado = itemPedidoRepository.findByPedido_IdAndProduto_Id(1L, 2L);

        assertTrue(resultado.isEmpty());
        verify(itemPedidoRepository, times(1)).findByPedido_IdAndProduto_Id(1L, 2L);
    }
}