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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoRepository itemPedidoRepository, ProdutoRepository produtoRepository){
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public Page<Pedido> obterTodos(LocalDateTime dataDeCorte, Pageable pageable) {
        if(dataDeCorte == null) return pedidoRepository.findAll(pageable);
        return pedidoRepository.findAllByDataAfter(dataDeCorte, pageable);
    }

    public Pedido obterPor(Long id) throws NegocioException {
        return buscarPedidoOuLancarExcecao(id);
    }

    private Pedido buscarPedidoOuLancarExcecao(Long id) throws NegocioException {
        return pedidoRepository.findPedidoById(id)
                .orElseThrow(() -> new NegocioException(String.format("Pedido %s não encontrado", id), HttpStatus.NOT_FOUND));
    }

    public void criarPedido(Pedido pedido) throws NegocioException {
        validarExistenciaPedido(pedido.getId());
        validarCpfCliente(pedido.getCpfCliente());

        if(pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedido);
                validarExistenciaProduto(item.getProduto());
            }
        }
        pedidoRepository.save(pedido);
    }

    public void adicionarItem(Long idPedido, ItemPedido itemPedido) throws NegocioException {
        var pedido = buscarPedidoOuLancarExcecao(idPedido);
        validarExistenciaProduto(itemPedido.getProduto());
        itemPedido.setPedido(pedido);
        itemPedidoRepository.save(itemPedido);
    }

    public void excluirItem(Long idPedido, Long idProduto) throws NegocioException {
        var itemPedido = itemPedidoRepository.findByPedido_IdAndProduto_Id(idPedido, idProduto)
                .orElseThrow(() -> new NegocioException(String.format("Item do Pedido %s não existe", idPedido), HttpStatus.UNPROCESSABLE_ENTITY));
        itemPedidoRepository.delete(itemPedido);
    }

    public void cancelar(Long id) throws NegocioException {
        var pedido = buscarPedidoOuLancarExcecao(id);
        atualizarSituacaoPedido(pedido, Situacao.CANCELADO);
    }

    public void finalizar(Long id) throws NegocioException {
        var pedido = buscarPedidoOuLancarExcecao(id);
        atualizarSituacaoPedido(pedido, Situacao.FINALIZADO);
    }

    private void atualizarSituacaoPedido(Pedido pedido, Situacao situacao) {
        pedido.setSituacao(situacao);
        pedidoRepository.save(pedido);
    }

    private void validarExistenciaPedido(Long id) throws NegocioException {
        if(pedidoRepository.findPedidoById(id).isPresent())
            throw new NegocioException(String.format("Pedido %s já existe", id), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private void validarCpfCliente(String cpf) throws NegocioException {
        if(!CpfUtil.isValidCPF(cpf))
            throw new NegocioException("CPF inválido", HttpStatus.BAD_REQUEST);
    }

    private void validarExistenciaProduto(Produto produto) throws NegocioException {
        if(produtoRepository.findById(produto.getId()).isEmpty())
            throw new NegocioException(String.format("Produto %s não existe", produto.getId()), HttpStatus.BAD_REQUEST);
    }
}

