
package br.com.avaliacao.controller;

import br.com.avaliacao.controller.dto.ItemPedidoDTO;
import br.com.avaliacao.controller.dto.PedidoDTO;
import br.com.avaliacao.domain.service.PedidoService;
import br.com.avaliacao.support.convert.ItemPedidoConverter;
import br.com.avaliacao.support.convert.PedidoConverter;
import br.com.avaliacao.support.exception.NegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoConverter pedidoConverter = new PedidoConverter();
    private final ItemPedidoConverter itemPedidoConverter = new ItemPedidoConverter();

    public PedidoController(final PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_B')")
    @Operation(summary = "Recuperar uma lista de Pedidos", description = "Recuperar uma lista paginada de pedidos realizados. " +
            "É possível filtrar os pedidos a partir de uma data de corte (yyyy-MM-ddTHH:mm:ss) para evitar buscar todos os pedidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem sucedida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class)))
    })
    public Page<PedidoDTO> obterTodos(@RequestParam(value = "data-de-corte", required = false) String dataDeCorte,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "100") int size,
                                      @RequestParam(defaultValue = "id") String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        LocalDateTime localDateTime;

        try {
            localDateTime = getLocalDateTime(dataDeCorte);
        } catch (Exception e) {
            throw new RuntimeException("Formato da data inválido. O formato experado é: yyyy-MM-ddTHH:mm:ss");
        }

        var pedidos = pedidoService.obterTodos(localDateTime, pageable).getContent().stream().map(pedidoConverter::toDTO).toList();
        return new PageImpl<>(pedidos, pageable, pedidos.size());
    }

    private static LocalDateTime getLocalDateTime(String dataDeCorte) {
        return Optional.ofNullable(dataDeCorte)
                .filter(date -> !date.isBlank())
                .map(LocalDateTime::parse)
                .orElse(null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperar um pedido", description = "Recuperar um pedido caso exista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação bem sucedida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "403", description = "Requisição não autorizada"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_A')")
    public ResponseEntity<Object> obterPorId(@PathVariable @NotNull(message = "ID é obrigatório") Long id){
        try {
            return ResponseEntity.ok(pedidoConverter.toDTO(pedidoService.obterPor(id)));
        } catch (NegocioException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Salvar pedido", description = "Salvar o pedido com seus itens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido salvo com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Não foi possível salvar o pedido"),
            @ApiResponse(responseCode = "403", description = "Requisição não autorizada"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_A')")
    public ResponseEntity<Object> criarPedido(final @RequestBody @Valid PedidoDTO pedidoDTO){
        try {
            pedidoService.criarPedido(pedidoConverter.toEntity(pedidoDTO));
        } catch (NegocioException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/adicionarProduto")
    @Operation(summary = "Adicionar produto", description = "Adicionar produto ao pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item do Pedido salvo com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Não foi possível salvar o item do pedido"),
            @ApiResponse(responseCode = "403", description = "Requisição não autorizada"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_A')")
    public ResponseEntity<Object> adicionarProduto(final @PathVariable @NotNull(message = "ID é obrigatório") Long id,
                                                 final @RequestBody @Valid ItemPedidoDTO itemPedidoDTO){
        try {
            pedidoService.adicionarItem(id, itemPedidoConverter.toEntity(itemPedidoDTO));
        } catch (NegocioException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/removerProduto/{idProduto}")
    @Operation(summary = "Remover produto", description = "Remover produto do pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item do Pedido removido com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Não foi possível remover o item do pedido"),
            @ApiResponse(responseCode = "403", description = "Requisição não autorizada"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_A')")
    public ResponseEntity<Object> removerProduto(final @PathVariable @NotNull(message = "Id é obrigatório") Long id,
                                               final @PathVariable @NotNull(message = "Id Produto é obrigatório") Long idProduto){
        try {
            pedidoService.excluirItem(id, idProduto);
        } catch (NegocioException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancelar o pedido mudando o status para CANCELADO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Não foi possível cancelar o pedido"),
            @ApiResponse(responseCode = "403", description = "Requisição não autorizada"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_A')")
    public ResponseEntity<Object> cancelarPedido(final @PathVariable @NotNull(message = "ID é obrigatório") Long id){
        try {
            pedidoService.cancelar(id);
        } catch (NegocioException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar pedido", description = "Finalizar o pedido mudando o status para FINALIZADO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido finalizado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Não foi possível finalizar o pedido"),
            @ApiResponse(responseCode = "403", description = "Requisição não autorizada"),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    @PreAuthorize("hasRole('ROLE_CLIENTE_EXTERNO_A')")
    public ResponseEntity<Object> finalizarPedido(final @PathVariable @NotNull(message = "ID é obrigatório") Long id){
        try {
            pedidoService.finalizar(id);
        } catch (NegocioException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
