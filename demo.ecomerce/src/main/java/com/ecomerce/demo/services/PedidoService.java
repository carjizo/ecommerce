package com.ecomerce.demo.services;

import com.ecomerce.demo.dtos.PedidoDTO;
import com.ecomerce.demo.dtos.PedidoDetalladoDTO;
import com.ecomerce.demo.entities.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PedidoService {

    Mono<Pedido> save(PedidoDTO dto);
    Flux<Pedido> getAll();
    Mono<Pedido> update(String id, PedidoDTO dto);
    Mono<Void> delete(String id);
    Mono<PedidoDetalladoDTO> getById(String id);
}
