package com.ecomerce.demo.handler;

import com.ecomerce.demo.dtos.ClienteDTO;
import com.ecomerce.demo.entities.Cliente;
import com.ecomerce.demo.services.ClienteService;
import com.ecomerce.demo.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClienteHandler {

    private final ClienteService clienteService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> getAll(ServerRequest serverRequest){
        Flux<Cliente> clientes = clienteService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(clientes, Cliente.class);
    }

    public Mono<ServerResponse> getOne(ServerRequest serverRequest){
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        Mono<Cliente> cliente = clienteService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(cliente, Cliente.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<ClienteDTO> dtoMono = serverRequest.bodyToMono(ClienteDTO.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(clienteDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clienteService.save(clienteDTO), Cliente.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        Mono<ClienteDTO> dtoMono = serverRequest.bodyToMono(ClienteDTO.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(clienteDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clienteService.update(id, clienteDTO), Cliente.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest){
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(clienteService.delete(id), Cliente.class);
    }
}
