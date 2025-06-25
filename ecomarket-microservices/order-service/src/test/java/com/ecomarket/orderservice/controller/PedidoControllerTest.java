package com.ecomarket.orderservice.controller;

import com.ecomarket.orderservice.model.Pedido;
import com.ecomarket.orderservice.model.DetallePedido;
import com.ecomarket.orderservice.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testing a nivel de CONTROLLER para PedidoController
 * Utiliza MockMvc para simular requests HTTP
 */
@WebMvcTest(PedidoController.class)
@ActiveProfiles("test")
@DisplayName("PedidoController - Tests de Integración")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedidoEjemplo;
    private DetallePedido detalleEjemplo;
    private List<Pedido> listaPedidos;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        pedidoEjemplo = new Pedido();
        pedidoEjemplo.setPedidoId(1);
        pedidoEjemplo.setFechaDePedido("2025-06-23");
        pedidoEjemplo.setClienteId(1);
        pedidoEjemplo.setEstado("PENDIENTE");
        pedidoEjemplo.setDescuento(10.0);
        pedidoEjemplo.setMetodoPagoId(1);
        pedidoEjemplo.setUsuarioId(1);
        pedidoEjemplo.setSubtotal(100.0);
        pedidoEjemplo.setTotal(90.0);
        pedidoEjemplo.setDireccionEnvio("Av. Providencia 123");
        pedidoEjemplo.setCiudadEnvio("Santiago");
        pedidoEjemplo.setNotas("Entrega en horario de oficina");

        detalleEjemplo = new DetallePedido();
        detalleEjemplo.setDetalleId(1);
        detalleEjemplo.setPedidoId(1);
        detalleEjemplo.setProductoId(1);
        detalleEjemplo.setPrecioUnitario(50.0);
        detalleEjemplo.setCantidad(2);
        detalleEjemplo.setSubTotal(100.0);

        listaPedidos = Arrays.asList(pedidoEjemplo);
    }

    @Test
    @DisplayName("GET /api/v1/pedidos - Debe obtener todos los pedidos (V1)")
    void testObtenerTodos() throws Exception {
        // Arrange
        when(pedidoService.obtenerTodos()).thenReturn(listaPedidos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/pedidos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].pedidoId", is(pedidoEjemplo.getPedidoId())));
    }

    // Puedes agregar más tests para los otros endpoints de PedidoController aquí
}