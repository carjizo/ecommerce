package com.ecomerce.demo.services;

import com.ecomerce.demo.dtos.ProductoDTO;
import com.ecomerce.demo.entities.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<Producto> getAll();
    Mono<Producto> getById(Long id);
    Mono<Producto> save(ProductoDTO dto);
    Mono<Producto> update(Long id, ProductoDTO dto);
    Mono<Void> delete(Long id);
}
