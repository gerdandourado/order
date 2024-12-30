package br.com.avaliacao.domain.repository;

import br.com.avaliacao.domain.model.entity.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoRepositoryTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoRepositoryTest pedidoRepositoryTest;

    @Test
    void dadoPageable_quandoFindByAll_entaoRetornaPaginaDePedidos() {
        PageRequest pageable = PageRequest.of(0, 10);
        Pedido pedido = Pedido.builder().id(1L).build();
        Page<Pedido> pedidoPage = new PageImpl<>(List.of(pedido));

        when(pedidoRepository.findAll(pageable)).thenReturn(pedidoPage);

        Page<Pedido> resultado = pedidoRepository.findAll(pageable);

        assertEquals(1, resultado.getContent().size());
        assertEquals(1L, resultado.getContent().get(0).getId());
        verify(pedidoRepository, times(1)).findAll(pageable);
    }

    @Test
    void dadoIdValido_quandoFindPedidoById_entaoRetornaPedido() {
        Pedido pedido = Pedido.builder().id(1L).build();
        when(pedidoRepository.findPedidoById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> resultado = pedidoRepository.findPedidoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(pedidoRepository, times(1)).findPedidoById(1L);
    }

    @Test
    void dadoIdInvalido_quandoFindPedidoById_entaoRetornaVazio() {
        when(pedidoRepository.findPedidoById(2L)).thenReturn(Optional.empty());

        Optional<Pedido> resultado = pedidoRepository.findPedidoById(2L);

        assertTrue(resultado.isEmpty());
        verify(pedidoRepository, times(1)).findPedidoById(2L);
    }
}