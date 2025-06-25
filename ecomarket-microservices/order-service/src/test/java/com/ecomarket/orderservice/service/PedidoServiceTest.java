package com.ecomarket.orderservice.service;

import com.ecomarket.orderservice.model.Pedido;
import com.ecomarket.orderservice.model.DetallePedido;
import com.ecomarket.orderservice.repository.PedidoRepository;
import com.ecomarket.orderservice.repository.DetallePedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testing a nivel de SERVICIO para PedidoService
 * Utiliza Mockito para simular dependencias (repositorios)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService - Tests Unitarios")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    
    @Mock
    private DetallePedidoRepository detallePedidoRepository;
    
    @InjectMocks
    private PedidoService pedidoService;
    
    private Pedido pedidoEjemplo;
    private DetallePedido detalleEjemplo;
    private List<DetallePedido> detallesEjemplo;

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
        
        detallesEjemplo = Arrays.asList(detalleEjemplo);
    }

    @Test
    @DisplayName("Debe obtener todos los pedidos correctamente")
    void testObtenerTodos() {
        // Arrange - Configurar el mock
        List<Pedido> pedidosEsperados = Arrays.asList(pedidoEjemplo);
        when(pedidoRepository.findAll()).thenReturn(pedidosEsperados);
        
        // Act - Ejecutar el método
        List<Pedido> pedidosObtenidos = pedidoService.obtenerTodos();
        
        // Assert - Verificar resultados
        assertNotNull(pedidosObtenidos);
        assertEquals(1, pedidosObtenidos.size());
        assertEquals("PENDIENTE", pedidosObtenidos.get(0).getEstado());
        assertEquals("Santiago", pedidosObtenidos.get(0).getCiudadEnvio());
        
        // Verificar que se llamó al repositorio
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe buscar pedido por ID correctamente")
    void testBuscarPorId() {
        // Arrange
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidoEjemplo));
        
        // Act
        Pedido pedidoEncontrado = pedidoService.buscarPorId(1);
        
        // Assert
        assertNotNull(pedidoEncontrado);
        assertEquals(1, pedidoEncontrado.getPedidoId());
        assertEquals("PENDIENTE", pedidoEncontrado.getEstado());
        assertEquals(90.0, pedidoEncontrado.getTotal());
        
        verify(pedidoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe retornar null cuando no encuentra pedido por ID")
    void testBuscarPorIdNoEncontrado() {
        // Arrange
        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        Pedido pedidoEncontrado = pedidoService.buscarPorId(999);
        
        // Assert
        assertNull(pedidoEncontrado);
        
        verify(pedidoRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Debe crear pedido con detalles y calcular totales correctamente")
    void testCrearPedido() {
        // Arrange
        Pedido pedidoNuevo = new Pedido();
        pedidoNuevo.setDescuento(10.0);
        
        DetallePedido detalle1 = new DetallePedido();
        detalle1.setPrecioUnitario(50.0);
        detalle1.setCantidad(2);
        
        DetallePedido detalle2 = new DetallePedido();
        detalle2.setPrecioUnitario(30.0);
        detalle2.setCantidad(1);
        
        List<DetallePedido> detalles = Arrays.asList(detalle1, detalle2);
        
        // Configurar mocks
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoEjemplo);
        when(detallePedidoRepository.save(any(DetallePedido.class))).thenReturn(detalleEjemplo);
        
        // Act
        Pedido pedidoCreado = pedidoService.crearPedido(pedidoNuevo, detalles);
        
        // Assert
        assertNotNull(pedidoCreado);
        assertEquals(130.0, pedidoNuevo.getSubtotal()); // 50*2 + 30*1 = 130
        assertEquals(117.0, pedidoNuevo.getTotal()); // 130 - (130 * 10 / 100) = 117
        
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(detallePedidoRepository, times(2)).save(any(DetallePedido.class));
    }

    @Test
    @DisplayName("Debe actualizar pedido existente")
    void testActualizar() {
        // Arrange
        when(pedidoRepository.existsById(1)).thenReturn(true);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoEjemplo);
        
        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setEstado("COMPLETADO");
        
        // Act
        Pedido resultado = pedidoService.actualizar(1, pedidoActualizado);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, pedidoActualizado.getPedidoId());
        
        verify(pedidoRepository, times(1)).existsById(1);
        verify(pedidoRepository, times(1)).save(pedidoActualizado);
    }

    @Test
    @DisplayName("Debe retornar null al actualizar pedido inexistente")
    void testActualizarPedidoInexistente() {
        // Arrange
        when(pedidoRepository.existsById(999)).thenReturn(false);
        
        Pedido pedidoActualizado = new Pedido();
        
        // Act
        Pedido resultado = pedidoService.actualizar(999, pedidoActualizado);
        
        // Assert
        assertNull(resultado);
        
        verify(pedidoRepository, times(1)).existsById(999);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Debe actualizar estado del pedido")
    void testActualizarEstado() {
        // Arrange
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidoEjemplo));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoEjemplo);
        
        // Act
        Pedido resultado = pedidoService.actualizarEstado(1, "COMPLETADO");
        
        // Assert
        assertNotNull(resultado);
        assertEquals("COMPLETADO", pedidoEjemplo.getEstado());
        
        verify(pedidoRepository, times(1)).findById(1);
        verify(pedidoRepository, times(1)).save(pedidoEjemplo);
    }

    @Test
    @DisplayName("Debe eliminar pedido con sus detalles")
    void testEliminar() {
        // Arrange
        when(pedidoRepository.existsById(1)).thenReturn(true);
        when(detallePedidoRepository.findByPedidoId(1)).thenReturn(detallesEjemplo);
        doNothing().when(detallePedidoRepository).deleteAll(detallesEjemplo);
        doNothing().when(pedidoRepository).deleteById(1);
        
        // Act
        boolean resultado = pedidoService.eliminar(1);
        
        // Assert
        assertTrue(resultado);
        
        verify(pedidoRepository, times(1)).existsById(1);
        verify(detallePedidoRepository, times(1)).findByPedidoId(1);
        verify(detallePedidoRepository, times(1)).deleteAll(detallesEjemplo);
        verify(pedidoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Debe obtener pedidos por cliente")
    void testObtenerPorCliente() {
        // Arrange
        List<Pedido> pedidosCliente = Arrays.asList(pedidoEjemplo);
        when(pedidoRepository.findByClienteId(1)).thenReturn(pedidosCliente);
        
        // Act
        List<Pedido> resultado = pedidoService.obtenerPorCliente(1);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getClienteId());
        
        verify(pedidoRepository, times(1)).findByClienteId(1);
    }

    @Test
    @DisplayName("Debe obtener pedidos por estado")
    void testObtenerPorEstado() {
        // Arrange
        List<Pedido> pedidosPendientes = Arrays.asList(pedidoEjemplo);
        when(pedidoRepository.findByEstado("PENDIENTE")).thenReturn(pedidosPendientes);
        
        // Act
        List<Pedido> resultado = pedidoService.obtenerPorEstado("PENDIENTE");
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        
        verify(pedidoRepository, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    @DisplayName("Debe contar total de pedidos")
    void testTotalPedidos() {
        // Arrange
        when(pedidoRepository.count()).thenReturn(50L);
        
        // Act
        int total = pedidoService.totalPedidos();
        
        // Assert
        assertEquals(50, total);
        
        verify(pedidoRepository, times(1)).count();
    }

    @Test
    @DisplayName("Debe calcular total de compras por cliente")
    void testTotalComprasPorCliente() {
        // Arrange
        when(pedidoRepository.sumTotalByClienteId(1)).thenReturn(500.0);
        
        // Act
        Double total = pedidoService.totalComprasPorCliente(1);
        
        // Assert
        assertEquals(500.0, total);
        
        verify(pedidoRepository, times(1)).sumTotalByClienteId(1);
    }

    @Test
    @DisplayName("Debe manejar total nulo para cliente sin compras")
    void testTotalComprasPorClienteSinCompras() {
        // Arrange
        when(pedidoRepository.sumTotalByClienteId(999)).thenReturn(null);
        
        // Act
        Double total = pedidoService.totalComprasPorCliente(999);
        
        // Assert
        assertEquals(0.0, total);
        
        verify(pedidoRepository, times(1)).sumTotalByClienteId(999);
    }

    @Test
    @DisplayName("Debe obtener detalles de un pedido")
    void testObtenerDetallesPedido() {
        // Arrange
        when(detallePedidoRepository.findByPedidoId(1)).thenReturn(detallesEjemplo);
        
        // Act
        List<DetallePedido> resultado = pedidoService.obtenerDetallesPedido(1);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getPedidoId());
        
        verify(detallePedidoRepository, times(1)).findByPedidoId(1);
    }

    @Test
    void dummyTestForSurefire() {
        // Este test vacío asegura que la clase es detectada por Surefire
        assertTrue(true);
    }
}