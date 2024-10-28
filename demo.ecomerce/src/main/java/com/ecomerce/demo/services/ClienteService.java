package com.ecomerce.demo.services;

import com.ecomerce.demo.dtos.ClienteDTO;
import com.ecomerce.demo.entities.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteService {

    Flux<Cliente> getAll();
    Mono<Cliente> getById(Long id);
    Mono<Cliente> save(ClienteDTO dto);
    Mono<Cliente> update(Long id, ClienteDTO dto);
    Mono<Void> delete(Long id);
}
