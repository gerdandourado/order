package br.com.avaliacao.domain.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProdutoTest {

    @Test
    void dadoProdutoValido_quandoCriarProduto_entaoRetornaAtributosCorretos() {
        Produto produto = Produto.builder()
                .id(1L)
                .descricao("Produto Teste")
                .build();

        assertEquals(1L, produto.getId());
        assertEquals("Produto Teste", produto.getDescricao());
    }

    @Test
    void dadoProdutoSemDescricao_quandoCriarProduto_entaoDescricaoEhNula() {
        Produto produto = Produto.builder()
                .id(2L)
                .build();

        assertEquals(2L, produto.getId());
        assertEquals(null, produto.getDescricao());
    }
}