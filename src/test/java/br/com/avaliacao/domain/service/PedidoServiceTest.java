package br.com.avaliacao.domain.service;

import br.com.avaliacao.domain.model.entity.ItemPedido;
import br.com.avaliacao.domain.model.entity.Pedido;
import br.com.avaliacao.domain.model.entity.Produto;
import br.com.avaliacao.domain.model.entity.Situacao;
import br.com.avaliacao.domain.repository.ItemPedidoRepository;
import br.com.avaliacao.domain.repository.PedidoRepository;
import br.com.avaliacao.domain.repository.ProdutoRepository;
import br.com.avaliacao.support.exception.NegocioException;
import br.com.avaliacao.support.util.CpfUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    private MockedStatic<CpfUtil> mockedCpfUtil;

    @BeforeEach
    void setupMockStatic() {
        mockedCpfUtil = mockStatic(CpfUtil.class);
    }

    @AfterEach
    void tearDownMockStatic() {
        mockedCpfUtil.close();
    }

    @Test
    void dadoDataDeCorteNula_quandoObterTodos_entaoRetornaTodosPedidos() {
        Pageable pageable = Pageable.ofSize(10);
        Page<Pedido> pedidos = new PageImpl<>(List.of(new Pedido()));

        when(pedidoRepository.findAll(pageable)).thenReturn(pedidos);

        Page<Pedido> result = pedidoService.obterTodos(null, pageable);

        assertTrue(result.hasContent());
        verify(pedidoRepository, times(1)).findAll(pageable);
    }

    @Test
    void dadoDataDeCorteValida_quandoObterTodos_entaoRetornaPedidosFiltrados() {
        Pageable pageable = Pageable.ofSize(10);
        LocalDate dataDeCorte = LocalDate.now();
        Page<Pedido> pedidos = new PageImpl<>(List.of(new Pedido()));

        when(pedidoRepository.findAllByDataAfter(dataDeCorte, pageable)).thenReturn(pedidos);

        Page<Pedido> result = pedidoService.obterTodos(dataDeCorte, pageable);

        assertTrue(result.hasContent());
        verify(pedidoRepository, times(1)).findAllByDataAfter(dataDeCorte, pageable);
    }

    @Test
    void dadoIdValido_quandoObterPor_entaoRetornaPedido() throws NegocioException {
        Pedido pedido = new Pedido();

        when(pedidoRepository.findPedidoById(1L)).thenReturn(Optional.of(pedido));

        Pedido result = pedidoService.obterPor(1L);

        assertTrue(result != null);
        verify(pedidoRepository, times(1)).findPedidoById(1L);
    }

    @Test
    void dadoIdInvalido_quandoObterPor_entaoLancaExcecao() {
        when(pedidoRepository.findPedidoById(1L)).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> pedidoService.obterPor(1L));
    }

    @Test
    void dadoNovoPedido_quandoCriarPedido_entaoSalvaPedido() throws NegocioException {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCpfCliente("12345678909");
        Produto produto = new Produto();
        produto.setId(1L);
        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        pedido.setItens(List.of(item));

        when(pedidoRepository.findPedidoById(pedido.getId())).thenReturn(Optional.empty());
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        mockedCpfUtil.when(() -> CpfUtil.isValidCPF(pedido.getCpfCliente())).thenReturn(true);

        // Chamada do método que será testado
        pedidoService.criarPedido(pedido);

        // Verificar se o pedido foi salvo corretamente
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void dadoPedidoComCpfInvalido_quandoCriarPedido_entaoLancaExcecao() {
        Pedido pedido = new Pedido();
        pedido.setCpfCliente("cpf_invalido"); // CPF deliberadamente inválido

        // Configurando o comportamento do mock global
        mockedCpfUtil.when(() -> CpfUtil.isValidCPF(pedido.getCpfCliente()))
                .thenReturn(false);

        // Act & Assert: Verifica se lançar a exceção é o comportamento esperado
        NegocioException exception = assertThrows(NegocioException.class, () -> pedidoService.criarPedido(pedido));

        // Verifica se a mensagem da exceção está correta
        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    void dadoProdutoInexistente_quandoAdicionarItem_entaoLancaExcecao() {
        Long pedidoId = 1L;
        Produto produto = new Produto();
        produto.setId(1L);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);

        // Mock do pedido existente
        when(pedidoRepository.findPedidoById(pedidoId)).thenReturn(Optional.of(pedido));

        // Mock do produto inexistente
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());

        // Act & Assert: Verifica se a exceção esperada é lançada
        NegocioException exception = assertThrows(NegocioException.class,
                () -> pedidoService.adicionarItem(pedidoId, itemPedido));

        // Assert: Mensagem da exceção e interações com os mocks
        assertEquals(String.format("Produto %s não existe", produto.getId()), exception.getMessage());
        verify(pedidoRepository, times(1)).findPedidoById(pedidoId);
        verify(produtoRepository, times(1)).findById(produto.getId());
        verifyNoInteractions(itemPedidoRepository);
    }

    @Test
    void dadoProdutoValido_quandoAdicionarItem_entaoSalvaItemPedido() throws NegocioException {
        Pedido pedido = new Pedido();
        Produto produto = new Produto();
        produto.setId(1L);
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);

        when(pedidoRepository.findPedidoById(1L)).thenReturn(Optional.of(pedido));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

        pedidoService.adicionarItem(1L, itemPedido);

        verify(itemPedidoRepository, times(1)).save(itemPedido);
    }

    @Test
    void dadoItemExistente_quandoExcluirItem_entaoDeletaItem() throws NegocioException {
        ItemPedido itemPedido = new ItemPedido();

        when(itemPedidoRepository.findByPedido_IdAndProduto_Id(1L, 1L)).thenReturn(Optional.of(itemPedido));

        pedidoService.excluirItem(1L, 1L);

        verify(itemPedidoRepository, times(1)).delete(itemPedido);
    }

    @Test
    void dadoItemInexistente_quandoExcluirItem_entaoLancaExcecao() {
        when(itemPedidoRepository.findByPedido_IdAndProduto_Id(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> pedidoService.excluirItem(1L, 1L));
    }

    @Test
    void dadoPedidoValido_quandoCancelarPedido_entaoAtualizaSituacao() throws NegocioException {
        Pedido pedido = new Pedido();

        when(pedidoRepository.findPedidoById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelar(1L);

        verify(pedidoRepository, times(1)).save(pedido);
        assertTrue(pedido.getSituacao() == Situacao.CANCELADO);
    }

    @Test
    void dadoPedidoValido_quandoFinalizarPedido_entaoAtualizaSituacao() throws NegocioException {
        Pedido pedido = new Pedido();

        when(pedidoRepository.findPedidoById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.finalizar(1L);

        verify(pedidoRepository, times(1)).save(pedido);
        assertTrue(pedido.getSituacao() == Situacao.FINALIZADO);
    }
}