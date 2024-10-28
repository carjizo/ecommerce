package com.ecomerce.demo.repositories;

import com.ecomerce.demo.entities.Pedido;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends ReactiveMongoRepository<Pedido, String> {
}
