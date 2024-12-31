package br.com.avaliacao.domain.repository;

import br.com.avaliacao.domain.model.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findAllByDataAfter(LocalDateTime data, Pageable pageable);

    Optional<Pedido> findPedidoById(Long id);
}
