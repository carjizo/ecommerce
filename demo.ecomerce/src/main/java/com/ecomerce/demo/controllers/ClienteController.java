//package com.ecomerce.demo.controllers;
//
//import com.ecomerce.demo.entities.Cliente;
//import com.ecomerce.demo.services.ClienteService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/cliente")
//@Slf4j
//@RequiredArgsConstructor
//public class ClienteController {
//
//    private final ClienteService clienteService;
//
//    @GetMapping
//    public Flux<Cliente> getAll(){
//        return clienteService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public Mono<Cliente> getById(@PathVariable("id") Long id){
//        return clienteService.getById(id);
//    }
//
//    @PostMapping
//    public Mono<Cliente> save(@RequestBody Cliente cliente){
//        return clienteService.save(cliente);
//    }
//
//    @PutMapping("/{id}")
//    public Mono<Cliente> update(@PathVariable("id") Long id, @RequestBody Cliente cliente){
//        return clienteService.update(id, cliente);
//    }
//
//    @DeleteMapping("/{id}")
//    public Mono<Void> delete(@PathVariable("id") Long id){
//        return clienteService.delete(id);
//    }
//}
