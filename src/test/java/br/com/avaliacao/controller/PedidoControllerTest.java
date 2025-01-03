package br.com.avaliacao.controller;

import br.com.avaliacao.controller.dto.ItemPedidoDTO;
import br.com.avaliacao.controller.dto.PedidoDTO;
import br.com.avaliacao.domain.model.entity.Pedido;
import br.com.avaliacao.domain.model.entity.Situacao;
import br.com.avaliacao.domain.service.PedidoService;
import br.com.avaliacao.support.convert.ItemPedidoConverter;
import br.com.avaliacao.support.convert.PedidoConverter;
import br.com.avaliacao.support.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {


    @Mock
    private PedidoService pedidoService;

    @Mock
    private PedidoConverter pedidoConverter;

    @Mock
    private ItemPedidoConverter itemPedidoConverter;

    @InjectMocks
    private PedidoController pedidoController;

    private PedidoDTO pedidoDTO;
    private ItemPedidoDTO itemPedidoDTO;

    @BeforeEach
    void setUp() {
        itemPedidoDTO = new ItemPedidoDTO(1L, null, BigDecimal.valueOf(2), BigDecimal.valueOf(3.5));
        pedidoDTO = new PedidoDTO(
                1L,
                "12345678901",
                LocalDateTime.parse("2024-12-30T12:00:00"),
                Situacao.CRIADO,
                null,
                List.of(itemPedidoDTO)
        );
    }

    @Test
    void dadoIdInvalido_quandoObterPorId_entaoRetornaErro() throws NegocioException {
        when(pedidoService.obterPor(1L)).thenThrow(new NegocioException("Pedido não encontrado", HttpStatus.NOT_FOUND));

        ResponseEntity<Object> resultado = pedidoController.obterPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        verify(pedidoService, times(1)).obterPor(1L);
    }

    @Test
    void dadoPedidoValido_quandoCriarPedido_entaoRetornaStatusCriado() throws NegocioException {
        ResponseEntity<Object> resultado = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        verify(pedidoService, times(1)).criarPedido(any());
    }

    @Test
    void dadoPedidoInvalido_quandoCriarPedido_entaoRetornaErro() throws NegocioException {
        doThrow(new NegocioException("Erro ao criar pedido", HttpStatus.BAD_REQUEST)).when(pedidoService).criarPedido(any());

        ResponseEntity<Object> resultado = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test
    void dadoIdEItemValidos_quandoAdicionarProduto_entaoAdicionaComSucesso() throws NegocioException {
        ResponseEntity<Object> resultado = pedidoController.adicionarProduto(1L, itemPedidoDTO);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(pedidoService, times(1)).adicionarItem(eq(1L), any());
    }

    @Test
    void dadoIdEItemInvalidos_quandoAdicionarProduto_entaoRetornaErro() throws NegocioException {
        doThrow(new NegocioException("Erro ao adicionar item", HttpStatus.BAD_REQUEST)).when(pedidoService).adicionarItem(anyLong(), any());

        ResponseEntity<Object> resultado = pedidoController.adicionarProduto(1L, itemPedidoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test
    void dadoIdEItemValidos_quandoRemoverProduto_entaoRemoveComSucesso() throws NegocioException {
        ResponseEntity<Object> resultado = pedidoController.removerProduto(1L, 2L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(pedidoService, times(1)).excluirItem(1L, 2L);
    }

    @Test
    void dadoIdEItemInvalidos_quandoRemoverProduto_entaoRetornaErro() throws NegocioException {
        doThrow(new NegocioException("Erro ao remover item", HttpStatus.BAD_REQUEST)).when(pedidoService).excluirItem(anyLong(), anyLong());

        ResponseEntity<Object> resultado = pedidoController.removerProduto(1L, 2L);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test
    void dadoIdValido_quandoCancelarPedido_entaoCancelaComSucesso() throws NegocioException {
        ResponseEntity<Object> resultado = pedidoController.cancelarPedido(1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(pedidoService, times(1)).cancelar(1L);
    }

    @Test
    void dadoIdInvalido_quandoCancelarPedido_entaoRetornaErro() throws NegocioException {
        doThrow(new NegocioException("Erro ao cancelar pedido", HttpStatus.BAD_REQUEST)).when(pedidoService).cancelar(anyLong());

        ResponseEntity<Object> resultado = pedidoController.cancelarPedido(1L);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }

    @Test
    void dadoIdValido_quandoFinalizarPedido_entaoFinalizaComSucesso() throws NegocioException {
        ResponseEntity<Object> resultado = pedidoController.finalizarPedido(1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(pedidoService, times(1)).finalizar(1L);
    }

    @Test
    void dadoIdInvalido_quandoFinalizarPedido_entaoRetornaErro() throws NegocioException {
        doThrow(new NegocioException("Erro ao finalizar pedido", HttpStatus.BAD_REQUEST)).when(pedidoService).finalizar(anyLong());

        ResponseEntity<Object> resultado = pedidoController.finalizarPedido(1L);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
    }
}