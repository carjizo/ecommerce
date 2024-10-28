package com.ecomerce.demo.services.impl;

import com.ecomerce.demo.dtos.PedidoDTO;
import com.ecomerce.demo.dtos.PedidoDetalladoDTO;
import com.ecomerce.demo.entities.Pedido;
import com.ecomerce.demo.exception.CustomException;
import com.ecomerce.demo.repositories.ClienteRepository;
import com.ecomerce.demo.repositories.PedidoRepository;
import com.ecomerce.demo.repositories.ProductoRepository;
import com.ecomerce.demo.services.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    private final static String EXISTS_CLIENT = "No existe el cliente";
    private final static String EXISTS_PEDIDO = "No existe el pedido";
    private final static String EXISTS_PRODUCT = "Producto no encontrado: ";
    private final static String INSUFI_PRODUCT = "Stock insuficiente para producto ";

    public Mono<Pedido> save(PedidoDTO dto) {
//        Mono<Boolean> existsClienteId = clienteRepository.findById(dto.getClienteId()).hasElement();
        return clienteRepository.findById(dto.getClienteId())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_CLIENT)))
                .flatMap(cliente ->
                        Flux.fromIterable(dto.getProductos())
                                .flatMap(pc ->
                                        productoRepository.findById(pc.getProductoId())
                                                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_PRODUCT + pc.getProductoId())))
                                                .flatMap(producto -> {
                                                    if (producto.getStock() < pc.getCantidad()) {
                                                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST,INSUFI_PRODUCT + pc.getProductoId()));
                                                    }
                                                    producto.setStock(producto.getStock() - pc.getCantidad());
                                                    return productoRepository.save(producto)
                                                            .thenReturn(producto.getPrecio() * pc.getCantidad());
                                                })
                                )
                                .reduce(Double::sum)
                                .flatMap(total -> {
                                    LocalDateTime fechaPedido = dto.getFechaPedido() != null
                                            ? dto.getFechaPedido()
                                            : ZonedDateTime.now(ZoneId.of("America/Lima")).toLocalDateTime();
                                    Pedido pedido = Pedido.builder()
                                            .clienteId(dto.getClienteId())
                                            .productos(dto.getProductos())
                                            .fechaPedido(fechaPedido)
                                            .total(total)
                                            .build();

                                    return pedidoRepository.save(pedido);
                                })
                );
    }

    public Flux<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    public Mono<Pedido> update(String id, PedidoDTO dto) {
        return pedidoRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_PEDIDO)))
                .flatMap(pedido -> {
                    Map<Long, Integer> productosActuales = new HashMap<>();
                    for (Pedido.ProductoCantidad pc : pedido.getProductos()) {
                        productosActuales.put(pc.getProductoId(), pc.getCantidad());
                    }

                    Map<Long, Integer> cambiosStock = new HashMap<>();

                    return Flux.fromIterable(dto.getProductos())
                            .flatMap(pc -> productoRepository.findById(pc.getProductoId())
                                    .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_PRODUCT + pc.getProductoId())))
                                    .flatMap(productoEncontrado -> {
                                        int nuevaCantidad = pc.getCantidad();
                                        int cantidadActual = productosActuales.getOrDefault(pc.getProductoId(), 0);
                                        int diferencia = nuevaCantidad - cantidadActual;

                                        if (diferencia > 0) {
                                            if (productoEncontrado.getStock() < diferencia) {
                                                return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, INSUFI_PRODUCT + pc.getProductoId()));
                                            }
                                            cambiosStock.put(pc.getProductoId(), -diferencia);
                                        } else if (diferencia < 0) {
                                            cambiosStock.put(pc.getProductoId(), -diferencia);
                                        }

                                        return Mono.just(productoEncontrado);
                                    })
                            )
                            .doOnNext(producto -> {
                                cambiosStock.forEach((productoId, cantidadCambio) -> {
                                    productoRepository.findById(productoId)
                                            .doOnNext(productoAActualizar -> {
                                                productoAActualizar.setStock(productoAActualizar.getStock() + cantidadCambio);
                                                productoRepository.save(productoAActualizar).subscribe();
                                            }).subscribe();
                                });
                            })
                            .collectList()
                            .flatMap(updatedProducts -> {
                                pedido.setClienteId(dto.getClienteId());
                                pedido.setProductos(dto.getProductos());
                                pedido.setTotal(dto.getTotal());
                                LocalDateTime fechaPedido = dto.getFechaPedido() != null
                                        ? dto.getFechaPedido()
                                        : ZonedDateTime.now(ZoneId.of("America/Lima")).toLocalDateTime();
                                pedido.setFechaPedido(fechaPedido);
                                return pedidoRepository.save(pedido);
                            });
                });
    }

    public Mono<Void> delete(String id) {
        return pedidoRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_PEDIDO)))
                .flatMap(pedido -> {
                    return Flux.fromIterable(pedido.getProductos())
                            .flatMap(pc -> productoRepository.findById(pc.getProductoId())
                                    .flatMap(producto -> {
                                        producto.setStock(producto.getStock() + pc.getCantidad());
                                        return productoRepository.save(producto);
                                    })
                            )
                            .then(pedidoRepository.deleteById(id));
                });
    }

    public Mono<PedidoDetalladoDTO> getById(String id) {
        return pedidoRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_PEDIDO)))
                .flatMap(pedido -> clienteRepository.findById(pedido.getClienteId())
                        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_CLIENT)))
                        .flatMap(cliente -> {
                            return Flux.fromIterable(pedido.getProductos())
//                                    .flatMap(pc -> productoRepository.findById(pc.getProductoId())
//                                            .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + pc.getProductoId())))
//                                            .map(producto -> new PedidoDetalladoDTO.ProductoDetalle(
//                                                    producto.getId(),
//                                                    producto.getNombre(),
//                                                    producto.getDescripcion(),
//                                                    producto.getPrecio(),
//                                                    pc.getCantidad()
//                                            ))
//                                    )
                                    .flatMap(pc -> productoRepository.findById(pc.getProductoId())
                                            .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, EXISTS_PRODUCT + pc.getProductoId())))
                                            .map(producto -> new PedidoDetalladoDTO.ProductoDetalle(
                                                    producto.getId(),
                                                    producto.getNombre(),
                                                    producto.getDescripcion(),
                                                    producto.getPrecio()
                                            ))
                                    )
                                    .collectList()
                                    .map(productosDetallados -> new PedidoDetalladoDTO(
                                            pedido.getId(),
                                            cliente.getId(),
                                            cliente.getNombre(),
                                            cliente.getEmail(),
                                            cliente.getTelefono(),
                                            cliente.getDireccion(),
                                            productosDetallados,
                                            pedido.getTotal(),
                                            pedido.getFechaPedido()
                                    ));
                        })
                );
    }
}
