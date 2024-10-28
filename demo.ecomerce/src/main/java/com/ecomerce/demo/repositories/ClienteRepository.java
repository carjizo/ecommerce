package com.ecomerce.demo.repositories;

import com.ecomerce.demo.entities.Cliente;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Long> {

    Mono<Cliente> findByEmail(String email);

    @Query("SELECT * FROM clientes WHERE id <> :id AND email = :email")
    Mono<Cliente> repeatedEmail(Long id, String email);
}
