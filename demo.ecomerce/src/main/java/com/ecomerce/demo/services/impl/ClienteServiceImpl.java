package com.ecomerce.demo.services.impl;

import com.ecomerce.demo.dtos.ClienteDTO;
import com.ecomerce.demo.entities.Cliente;
import com.ecomerce.demo.exception.CustomException;
import com.ecomerce.demo.repositories.ClienteRepository;
import com.ecomerce.demo.services.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor //not Autowired
public class ClienteServiceImpl implements ClienteService {

    private final static String NF_MESSAGE = "cliente no existe";
    private final static String NOMBRE_MESSAGE = "email del cliente ya en uso";

    private final ClienteRepository clienteRepository;

    public Flux<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Mono<Cliente> getById(Long id){
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND,NF_MESSAGE)));
    }

    public Mono<Cliente> save(ClienteDTO dto) {
        Mono<Boolean> existsEmail = clienteRepository.findByEmail(dto.getEmail()).hasElement();
        return existsEmail.flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST,NOMBRE_MESSAGE))
                : clienteRepository
                .save(Cliente
                        .builder()
                        .nombre(dto.getNombre())
                        .email(dto.getEmail())
                        .direccion(dto.getDireccion())
                        .telefono(dto.getTelefono())
                        .build()));
    }

    public Mono<Cliente> update(Long id, ClienteDTO dto) {
        Mono<Boolean> clienteId = clienteRepository.findById(id).hasElement();
        Mono<Boolean> clienteRepeatedEmail = clienteRepository.repeatedEmail(id, dto.getEmail()).hasElement();
        return clienteId.flatMap(
                existsId -> existsId ? // si existe
                        clienteRepeatedEmail.flatMap(existsEmail -> existsEmail ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST,NOMBRE_MESSAGE))
                                : clienteRepository.save(new Cliente(id, dto.getNombre(), dto.getEmail(), dto.getTelefono(), dto.getDireccion())))
                        : Mono.error(new CustomException(HttpStatus.NOT_FOUND,NF_MESSAGE)));
    }

    public Mono<Void> delete(Long id) {
        Mono<Boolean> clienteId = clienteRepository.findById(id).hasElement();
        return clienteId.flatMap(exists -> exists ? clienteRepository.deleteById(id) : Mono.error(new CustomException(HttpStatus.NOT_FOUND,NF_MESSAGE)));
    }
}
