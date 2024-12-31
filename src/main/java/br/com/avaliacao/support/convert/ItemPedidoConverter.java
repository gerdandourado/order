package br.com.avaliacao.support.convert;

import br.com.avaliacao.controller.dto.ItemPedidoDTO;
import br.com.avaliacao.domain.model.entity.ItemPedido;
import br.com.avaliacao.domain.model.entity.Produto;

public class ItemPedidoConverter {

    public ItemPedido toEntity(ItemPedidoDTO itemPedidoDTO){
        return ItemPedido.builder()
                .quantidade(itemPedidoDTO.quantidade())
                .produto(Produto.builder().id(itemPedidoDTO.idProduto()).build())
                .valorUnitario(itemPedidoDTO.valorUnitario())
                .build();
    }

    public ItemPedidoDTO toDTO(ItemPedido itemPedido){
        return new ItemPedidoDTO(itemPedido.getProduto().getId(), itemPedido.getQuantidade(), itemPedido.getValorUnitario());
    }
}
