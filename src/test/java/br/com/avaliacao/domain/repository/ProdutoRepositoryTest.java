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

    @InjectMocks
    private ProdutoRepository produtoRepository;

    @Mock
    private JpaRepository<Produto, Long> jpaRepository;

    @Test
    void dadoProdutoIdValido_quandoProcurarPorId_entaoRetornaProduto() {
        Produto produto = new Produto();
        produto.setId(1L);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void dadoProdutoIdInvalido_quandoProcurarPorId_entaoRetornaOptionalVazio() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Produto> resultado = produtoRepository.findById(1L);

        assertTrue(resultado.isEmpty());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void dadoProdutoValido_quandoSalvar_entaoPersisteDados() {
        Produto produto = new Produto();

        when(jpaRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoRepository.save(produto);

        assertEquals(produto, resultado);
        verify(jpaRepository, times(1)).save(produto);
    }

    @Test
    void dadoProdutoId_quandoDeletar_entaoRemoveProduto() {
        produtoRepository.deleteById(1L);

        verify(jpaRepository, times(1)).deleteById(1L);
    }
}