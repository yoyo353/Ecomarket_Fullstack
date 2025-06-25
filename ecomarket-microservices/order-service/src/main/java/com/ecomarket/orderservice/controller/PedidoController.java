package com.ecomarket.orderservice.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Operaciones de gestión de pedidos en EcoMarket")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Operation(
        summary = "Obtener todos los pedidos",
        description = "Retorna una lista completa de todos los pedidos registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de pedidos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor"
        )
    })
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        return ResponseEntity.ok(pedidos);
    }
    
    @Operation(
        summary = "Obtener pedido por ID",
        description = "Busca y retorna un pedido específico utilizando su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pedido encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Pedido no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(
            @Parameter(description = "ID único del pedido", example = "1", required = true)
            @PathVariable Integer id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
        summary = "Crear nuevo pedido",
        description = "Crea un nuevo pedido con sus detalles asociados. Calcula automáticamente subtotales y totales."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Pedido creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Pedido.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<Pedido> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del pedido a crear con sus detalles",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        name = "Ejemplo de pedido",
                        value = """
                        {
                          "fechaDePedido": "2025-06-23",
                          "clienteId": 1,
                          "estado": "PENDIENTE",
                          "descuento": 10.0,
                          "metodoPagoId": 1,
                          "usuarioId": 1,
                          "direccionEnvio": "Av. Las Condes 123",
                          "ciudadEnvio": "Santiago",
                          "notas": "Entrega en horario de oficina",
                          "detalles": [
                            {
                              "productoId": 1,
                              "precioUnitario": 5.99,
                              "cantidad": 2
                            },
                            {
                              "productoId": 3,
                              "precioUnitario": 4.95,
                              "cantidad": 1
                            }
                          ]
                        }
                        """
                    )
                )
            )
            @RequestBody Map<String, Object> request) {
        try {
            // Extraer pedido del request
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
            
            // Extraer detalles del request
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
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(
        summary = "Actualizar pedido completo",
        description = "Actualiza todos los campos de un pedido existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pedido actualizado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Pedido no encontrado"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(
            @Parameter(description = "ID del pedido a actualizar", example = "1")
            @PathVariable Integer id, 
            @RequestBody Pedido pedido) {
        Pedido pedidoActualizado = pedidoService.actualizar(id, pedido);
        if (pedidoActualizado != null) {
            return ResponseEntity.ok(pedidoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
        summary = "Actualizar estado del pedido",
        description = "Cambia únicamente el estado de un pedido específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Estado actualizado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Pedido no encontrado"
        )
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @Parameter(description = "ID del pedido", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevo estado del pedido",
                content = @Content(
                    examples = @ExampleObject(
                        value = """
                        {
                          "estado": "COMPLETADO"
                        }
                        """
                    )
                )
            )
            @RequestBody Map<String, String> request) {
        String nuevoEstado = request.get("estado");
        Pedido pedidoActualizado = pedidoService.actualizarEstado(id, nuevoEstado);
        if (pedidoActualizado != null) {
            return ResponseEntity.ok(pedidoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
        summary = "Eliminar pedido",
        description = "Elimina un pedido y todos sus detalles asociados del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Pedido eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Pedido no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del pedido a eliminar", example = "1")
            @PathVariable Integer id) {
        boolean eliminado = pedidoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(
        summary = "Obtener detalles de un pedido",
        description = "Retorna todos los detalles (productos) asociados a un pedido específico"
    )
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetallePedido>> obtenerDetalles(
            @Parameter(description = "ID del pedido", example = "1")
            @PathVariable Integer id) {
        List<DetallePedido> detalles = pedidoService.obtenerDetallesPedido(id);
        return ResponseEntity.ok(detalles);
    }
    
    // ======================================================================
    // ENDPOINTS DE ESTADÍSTICAS Y CONSULTAS
    // ======================================================================
    
    @Operation(
        summary = "Obtener total de pedidos",
        description = "Retorna el número total de pedidos registrados en el sistema"
    )
    @GetMapping("/total")
    public ResponseEntity<Integer> totalPedidos() {
        int total = pedidoService.totalPedidos();
        return ResponseEntity.ok(total);
    }
    
    @Operation(
        summary = "Obtener pedidos por cliente",
        description = "Filtra y retorna todos los pedidos realizados por un cliente específico"
    )
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> obtenerPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Integer clienteId) {
        List<Pedido> pedidos = pedidoService.obtenerPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @Operation(
        summary = "Obtener pedidos por estado",
        description = "Filtra pedidos según su estado actual (PENDIENTE, COMPLETADO, etc.)"
    )
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> obtenerPorEstado(
            @Parameter(
                description = "Estado del pedido", 
                example = "COMPLETADO",
                schema = @Schema(allowableValues = {"PENDIENTE", "EN_PROCESO", "COMPLETADO", "CANCELADO", "ENVIADO"})
            )
            @PathVariable String estado) {
        List<Pedido> pedidos = pedidoService.obtenerPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }
    
    @Operation(
        summary = "Obtener pedidos por ciudad de envío",
        description = "Filtra pedidos según la ciudad de destino de envío"
    )
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Pedido>> obtenerPorCiudad(
            @Parameter(description = "Ciudad de envío", example = "Santiago")
            @PathVariable String ciudad) {
        List<Pedido> pedidos = pedidoService.obtenerPorCiudad(ciudad);
        return ResponseEntity.ok(pedidos);
    }
    
    @Operation(
        summary = "Obtener pedidos por rango de total",
        description = "Filtra pedidos cuyo total esté dentro de un rango específico de precios"
    )
    @GetMapping("/rango-total")
    public ResponseEntity<List<Pedido>> obtenerPorRangoTotal(
            @Parameter(description = "Total mínimo", example = "10.0")
            @RequestParam Double totalMin, 
            @Parameter(description = "Total máximo", example = "100.0")
            @RequestParam Double totalMax) {
        List<Pedido> pedidos = pedidoService.obtenerPorRangoTotal(totalMin, totalMax);
        return ResponseEntity.ok(pedidos);
    }
    
    @Operation(
        summary = "Obtener total de compras por cliente",
        description = "Calcula el monto total gastado por un cliente en pedidos completados"
    )
    @GetMapping("/cliente/{clienteId}/total-compras")
    public ResponseEntity<Double> totalComprasPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Integer clienteId) {
        Double total = pedidoService.totalComprasPorCliente(clienteId);
        return ResponseEntity.ok(total);
    }
    
    @Operation(
        summary = "Obtener cantidad de pedidos por cliente",
        description = "Cuenta el número total de pedidos realizados por un cliente"
    )
    @GetMapping("/cliente/{clienteId}/cantidad")
    public ResponseEntity<Long> cantidadPedidosPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Integer clienteId) {
        Long cantidad = pedidoService.cantidadPedidosPorCliente(clienteId);
        return ResponseEntity.ok(cantidad);
    }
    
    @Operation(
        summary = "Obtener productos más vendidos",
        description = "Retorna una lista de productos ordenados por cantidad vendida (mayor a menor)"
    )
    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<Object[]>> obtenerProductosMasVendidos() {
        List<Object[]> productos = pedidoService.obtenerProductosMasVendidos();
        return ResponseEntity.ok(productos);
    }
}
