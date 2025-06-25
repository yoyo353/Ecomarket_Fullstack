package com.ecomarket.orderservice.controller;

import com.ecomarket.orderservice.assembler.DetallePedidoModelAssembler;
import com.ecomarket.orderservice.assembler.PedidoModelAssembler;
import com.ecomarket.orderservice.model.Pedido;
import com.ecomarket.orderservice.model.DetallePedido;
import com.ecomarket.orderservice.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pedidos")
@Tag(name = "Pedidos V2 (HATEOAS)", description = "API de gestión de pedidos con enlaces hipermedia - Versión 2")
public class PedidoControllerV2 {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoModelAssembler pedidoAssembler;
    
    @Autowired
    private DetallePedidoModelAssembler detalleAssembler;
    
    @Operation(
        summary = "Obtener todos los pedidos (HATEOAS)",
        description = "Retorna una colección completa de pedidos con enlaces hipermedia para navegación dinámica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Colección de pedidos con enlaces HATEOAS obtenida exitosamente"
        )
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerTodos() {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerTodos().stream()
                .map(pedidoAssembler::toModelSimple)
                .collect(Collectors.toList());
        
        // Crear colección con enlaces
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        
        // Agregar enlace a sí mismo
        collectionModel.add(linkTo(PedidoControllerV2.class).withSelfRel());
        
        // Agregar enlaces relacionados
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).totalPedidos()).withRel("total"));
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).obtenerProductosMasVendidos()).withRel("productos-mas-vendidos"));
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @Operation(
        summary = "Obtener pedido por ID (HATEOAS)",
        description = "Busca un pedido específico y retorna todos sus enlaces hipermedia relacionados"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPorId(
            @Parameter(description = "ID único del pedido", example = "1")
            @PathVariable Integer id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedidoAssembler.toModel(pedido));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
        summary = "Crear nuevo pedido (HATEOAS)",
        description = "Crea un pedido y retorna el recurso con todos sus enlaces hipermedia"
    )
    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del pedido a crear",
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                        {
                          \"fechaDePedido\": \"2025-06-23\",
                          \"clienteId\": 1,
                          \"estado\": \"PENDIENTE\",
                          \"descuento\": 10.0,
                          \"metodoPagoId\": 1,
                          \"usuarioId\": 1,
                          \"direccionEnvio\": \"Av. Las Condes 456\",
                          \"ciudadEnvio\": \"Santiago\",
                          \"notas\": \"Pedido con HATEOAS\",
                          \"detalles\": [
                            {
                              \"productoId\": 1,
                              \"precioUnitario\": 15.99,
                              \"cantidad\": 2
                            }
                          ]
                        }
                        """
                    )
                )
            )
            @RequestBody Map<String, Object> request) {
        try {
            // Lógica de creación (mismo que V1)
            Pedido pedido = new Pedido();
            pedido.setFechaDePedido((String) request.get("fechaDePedido"));
            pedido.setClienteId((Integer) request.get("clienteId"));
            pedido.setEstado((String) request.get("estado"));
            pedido.setDescuento(request.get("descuento") != null ? 
                Double.valueOf(request.get("descuento").toString()) : 0.0);
            pedido.setMetodoPagoId((Integer) request.get("metodoPagoId"));
            pedido.setUsuarioId((Integer) request.get("usuarioId"));
            pedido.setDireccionEnvio((String) request.get("direccionEnvio"));
            pedido.setCiudadEnvio((String) request.get("ciudadEnvio"));
            pedido.setNotas((String) request.get("notas"));
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detallesData = (List<Map<String, Object>>) request.get("detalles");
            List<DetallePedido> detalles = detallesData.stream().map(detalleData -> {
                DetallePedido detalle = new DetallePedido();
                detalle.setProductoId((Integer) detalleData.get("productoId"));
                detalle.setPrecioUnitario(Double.valueOf(detalleData.get("precioUnitario").toString()));
                detalle.setCantidad((Integer) detalleData.get("cantidad"));
                return detalle;
            }).toList();
            
            Pedido nuevoPedido = pedidoService.crearPedido(pedido, detalles);
            
            // Retornar con enlaces HATEOAS
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoAssembler.toModel(nuevoPedido));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Actualizar pedido (HATEOAS)")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> actualizar(
            @PathVariable Integer id, 
            @RequestBody Pedido pedido) {
        Pedido pedidoActualizado = pedidoService.actualizar(id, pedido);
        if (pedidoActualizado != null) {
            return ResponseEntity.ok(pedidoAssembler.toModel(pedidoActualizado));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Actualizar estado del pedido (HATEOAS)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Pedido>> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, String> request) {
        String nuevoEstado = request.get("estado");
        Pedido pedidoActualizado = pedidoService.actualizarEstado(id, nuevoEstado);
        if (pedidoActualizado != null) {
            return ResponseEntity.ok(pedidoAssembler.toModel(pedidoActualizado));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Eliminar pedido (HATEOAS)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = pedidoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
        summary = "Obtener detalles de un pedido (HATEOAS)",
        description = "Retorna los detalles del pedido con enlaces hipermedia a productos y pedido padre"
    )
    @GetMapping("/{id}/detalles")
    public ResponseEntity<CollectionModel<EntityModel<DetallePedido>>> obtenerDetalles(
            @PathVariable Integer id) {
        List<EntityModel<DetallePedido>> detalles = pedidoService.obtenerDetallesPedido(id).stream()
                .map(detalleAssembler::toModel)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<DetallePedido>> collectionModel = CollectionModel.of(detalles);
        
        // Enlaces de la colección
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).obtenerDetalles(id)).withSelfRel());
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).obtenerPorId(id)).withRel("pedido"));
        
        return ResponseEntity.ok(collectionModel);
    }
    
    // ======================================================================
    // ENDPOINTS DE ESTADÍSTICAS CON HATEOAS
    // ======================================================================
    
    @Operation(summary = "Total de pedidos (HATEOAS)")
    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> totalPedidos() {
        int total = pedidoService.totalPedidos();
        
        // Crear respuesta con enlaces
        Map<String, Object> response = Map.of(
            "total", total,
            "_links", Map.of(
                "self", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).totalPedidos()).toString()),
                "pedidos", Map.of("href", linkTo(PedidoControllerV2.class).toString())
            )
        );
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener pedidos por cliente (HATEOAS)")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorCliente(
            @PathVariable Integer clienteId) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorCliente(clienteId).stream()
                .map(pedidoAssembler::toModelSimple)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).obtenerPorCliente(clienteId)).withSelfRel());
        collectionModel.add(linkTo(PedidoControllerV2.class).withRel("todos-los-pedidos"));
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).totalComprasPorCliente(clienteId)).withRel("total-compras"));
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).cantidadPedidosPorCliente(clienteId)).withRel("cantidad-pedidos"));
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @Operation(summary = "Obtener pedidos por estado (HATEOAS)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorEstado(
            @PathVariable String estado) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorEstado(estado).stream()
                .map(pedidoAssembler::toModelSimple)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).obtenerPorEstado(estado)).withSelfRel());
        collectionModel.add(linkTo(PedidoControllerV2.class).withRel("todos-los-pedidos"));
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @Operation(summary = "Obtener pedidos por ciudad (HATEOAS)")
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerPorCiudad(
            @PathVariable String ciudad) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorCiudad(ciudad).stream()
                .map(pedidoAssembler::toModelSimple)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        collectionModel.add(linkTo(methodOn(PedidoControllerV2.class).obtenerPorCiudad(ciudad)).withSelfRel());
        collectionModel.add(linkTo(PedidoControllerV2.class).withRel("todos-los-pedidos"));
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @Operation(summary = "Total de compras por cliente (HATEOAS)")
    @GetMapping("/cliente/{clienteId}/total-compras")
    public ResponseEntity<Map<String, Object>> totalComprasPorCliente(@PathVariable Integer clienteId) {
        Double total = pedidoService.totalComprasPorCliente(clienteId);
        
        Map<String, Object> response = Map.of(
            "clienteId", clienteId,
            "totalCompras", total,
            "_links", Map.of(
                "self", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).totalComprasPorCliente(clienteId)).toString()),
                "pedidos-cliente", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).obtenerPorCliente(clienteId)).toString()),
                "cantidad-pedidos", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).cantidadPedidosPorCliente(clienteId)).toString())
            )
        );
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Cantidad de pedidos por cliente (HATEOAS)")
    @GetMapping("/cliente/{clienteId}/cantidad")
    public ResponseEntity<Map<String, Object>> cantidadPedidosPorCliente(@PathVariable Integer clienteId) {
        Long cantidad = pedidoService.cantidadPedidosPorCliente(clienteId);
        
        Map<String, Object> response = Map.of(
            "clienteId", clienteId,
            "cantidadPedidos", cantidad,
            "_links", Map.of(
                "self", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).cantidadPedidosPorCliente(clienteId)).toString()),
                "pedidos-cliente", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).obtenerPorCliente(clienteId)).toString()),
                "total-compras", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).totalComprasPorCliente(clienteId)).toString())
            )
        );
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Productos más vendidos (HATEOAS)")
    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<Map<String, Object>> obtenerProductosMasVendidos() {
        List<Object[]> productos = pedidoService.obtenerProductosMasVendidos();
        
        Map<String, Object> response = Map.of(
            "productos", productos,
            "_links", Map.of(
                "self", Map.of("href", linkTo(methodOn(PedidoControllerV2.class).obtenerProductosMasVendidos()).toString()),
                "pedidos", Map.of("href", linkTo(PedidoControllerV2.class).toString())
            )
        );
        
        return ResponseEntity.ok(response);
    }
}
