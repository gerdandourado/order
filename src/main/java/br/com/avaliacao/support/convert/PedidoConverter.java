package br.com.avaliacao.support.convert;

import br.com.avaliacao.controller.dto.PedidoDTO;
import br.com.avaliacao.domain.model.entity.ItemPedido;
import br.com.avaliacao.domain.model.entity.Pedido;

import java.math.BigDecimal;

public class PedidoConverter {

    private final ItemPedidoConverter itemPedidoConverter = new ItemPedidoConverter();

    public Pedido toEntity(PedidoDTO pedidoDTO){

        var itens = pedidoDTO.itens() == null ? null : pedidoDTO.itens().stream().map(itemPedidoConverter::toEntity).toList();

        return Pedido.builder()
                .id(pedidoDTO.id())
                .cpfCliente(pedidoDTO.cpfCliente())
                .data(pedidoDTO.data())
                .situacao(pedidoDTO.situacao())
                .itens(itens)
                .build();
    }

    public PedidoDTO toDTO(Pedido pedido){

        var itens =  pedido.getItens() == null ? null : pedido.getItens().stream().map(itemPedidoConverter::toDTO).toList();
        BigDecimal valorTotal = pedido.getItens() == null ? null : pedido.getItens().stream().map(ItemPedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PedidoDTO(pedido.getId(),
                pedido.getCpfCliente(),
                pedido.getData(),
                pedido.getSituacao(),
                valorTotal,
                itens
        );
    }
}
