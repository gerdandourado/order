package br.com.avaliacao.domain.repository;

import br.com.avaliacao.domain.model.entity.Produto;
import br.com.avaliacao.domain.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoRepositoryTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void dadoIdValido_quandoFindById_entaoRetornaProduto() {
        Produto produto = Produto.builder().id(1L).descricao("Produto 1").build();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> result = produtoRepository.findById(1L);

        assertEquals(Optional.of(produto), result);
        verify(produtoRepository).findById(1L);
    }

    @Test
    void dadoProduto_quandoSave_entaoProdutoEhPersistido() {
        Produto produto = Produto.builder().descricao("Produto 2").build();
        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto result = produtoRepository.save(produto);

        assertEquals(produto, result);
        verify(produtoRepository).save(produto);
    }

    @Test
    void dadoIdValido_quandoDeleteById_entaoRemocaoEhRealizada() {
        doNothing().when(produtoRepository).deleteById(1L);

        produtoRepository.deleteById(1L);

        verify(produtoRepository).deleteById(1L);
    }
}