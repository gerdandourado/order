package br.com.avaliacao.domain.repository;

import br.com.avaliacao.domain.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
