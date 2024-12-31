package br.com.avaliacao.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    private Long id;
    @Column(name = "cpf_cliente")
    private String cpfCliente;
    private LocalDateTime data;
    private Situacao situacao;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    public BigDecimal getValorTotal(){

        if(itens == null) return BigDecimal.ZERO;
        return itens.stream().map(ItemPedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
