package com.ecomarket.productservice.service;

import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoEcologico;
    private Producto productoNoEcologico;

    @BeforeEach
    void setUp() {
        // Configurar productos de prueba
        productoEcologico = new Producto();
        productoEcologico.setProductoId(1);
        productoEcologico.setNombreProducto("Jabón de lavanda ecológico");
        productoEcologico.setCodigoSKU("ECO-001");
        productoEcologico.setPrecioUnitario(12.50);
        productoEcologico.setPrecioCompra(6.00);
        productoEcologico.setMargenGanancia(0.52);
        productoEcologico.setDescripcion("Jabón 100% natural");
        productoEcologico.setCategoriaId(1);
        productoEcologico.setProveedorPrincipalId(1);
        productoEcologico.setEsEcologico(true);
        productoEcologico.setFechaRegistro("2024-06-24 10:00:00");
        productoEcologico.setEstado("ACTIVE");

        productoNoEcologico = new Producto();
        productoNoEcologico.setProductoId(2);
        productoNoEcologico.setNombreProducto("Producto convencional");
        productoNoEcologico.setCodigoSKU("CONV-001");
        productoNoEcologico.setPrecioUnitario(8.99);
        productoNoEcologico.setPrecioCompra(4.50);
        productoNoEcologico.setMargenGanancia(0.50);
        productoNoEcologico.setDescripcion("Producto convencional");
        productoNoEcologico.setCategoriaId(2);
        productoNoEcologico.setProveedorPrincipalId(2);
        productoNoEcologico.setEsEcologico(false);
        productoNoEcologico.setFechaRegistro("2024-06-24 11:00:00");
        productoNoEcologico.setEstado("ACTIVE");
    }

    @Test
    void testObtenerTodos() {
        // Arrange
        List<Producto> productosEsperados = Arrays.asList(productoEcologico, productoNoEcologico);
        when(productRepository.findAll()).thenReturn(productosEsperados);

        // Act
        List<Producto> productos = productoService.obtenerTodos();

        // Assert
        assertNotNull(productos);
        assertEquals(2, productos.size());
        assertEquals("Jabón de lavanda ecológico", productos.get(0).getNombreProducto());
        assertEquals("Producto convencional", productos.get(1).getNombreProducto());
        
        // Verificar que el repositorio fue llamado una vez
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_ProductoExistente() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(productoEcologico));

        // Act
        Producto producto = productoService.buscarPorId(1);

        // Assert
        assertNotNull(producto);
        assertEquals(1, producto.getProductoId());
        assertEquals("ECO-001", producto.getCodigoSKU());
        assertEquals(true, producto.getEsEcologico());
        
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testBuscarPorId_ProductoNoExistente() {
        // Arrange
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Producto producto = productoService.buscarPorId(999);

        // Assert
        assertNull(producto);
        verify(productRepository, times(1)).findById(999);
    }

    @Test
    void testBuscarPorSKU_ProductoExistente() {
        // Arrange
        when(productRepository.findByCodigoSKU("ECO-001")).thenReturn(Optional.of(productoEcologico));

        // Act
        Producto producto = productoService.buscarPorSKU("ECO-001");

        // Assert
        assertNotNull(producto);
        assertEquals("ECO-001", producto.getCodigoSKU());
        assertEquals("Jabón de lavanda ecológico", producto.getNombreProducto());
        
        verify(productRepository, times(1)).findByCodigoSKU("ECO-001");
    }

    @Test
    void testBuscarPorSKU_ProductoNoExistente() {
        // Arrange
        when(productRepository.findByCodigoSKU("SKU-INEXISTENTE")).thenReturn(Optional.empty());

        // Act
        Producto producto = productoService.buscarPorSKU("SKU-INEXISTENTE");

        // Assert
        assertNull(producto);
        verify(productRepository, times(1)).findByCodigoSKU("SKU-INEXISTENTE");
    }

    @Test
    void testGuardar() {
        // Arrange
        when(productRepository.save(any(Producto.class))).thenReturn(productoEcologico);

        // Act
        Producto productoGuardado = productoService.guardar(productoEcologico);

        // Assert
        assertNotNull(productoGuardado);
        assertEquals("ECO-001", productoGuardado.getCodigoSKU());
        assertEquals(true, productoGuardado.getEsEcologico());
        
        verify(productRepository, times(1)).save(productoEcologico);
    }

    @Test
    void testActualizar_ProductoExistente() {
        // Arrange
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.save(any(Producto.class))).thenReturn(productoEcologico);

        // Act
        Producto productoActualizado = productoService.actualizar(1, productoEcologico);

        // Assert
        assertNotNull(productoActualizado);
        assertEquals(1, productoActualizado.getProductoId());
        
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).save(productoEcologico);
    }

    @Test
    void testActualizar_ProductoNoExistente() {
        // Arrange
        when(productRepository.existsById(999)).thenReturn(false);

        // Act
        Producto resultado = productoService.actualizar(999, productoEcologico);

        // Assert
        assertNull(resultado);
        
        verify(productRepository, times(1)).existsById(999);
        verify(productRepository, never()).save(any());
    }

    @Test
    void testEliminar_ProductoExistente() {
        // Arrange
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        // Act
        boolean eliminado = productoService.eliminar(1);

        // Assert
        assertTrue(eliminado);
        
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminar_ProductoNoExistente() {
        // Arrange
        when(productRepository.existsById(999)).thenReturn(false);

        // Act
        boolean eliminado = productoService.eliminar(999);

        // Assert
        assertFalse(eliminado);
        
        verify(productRepository, times(1)).existsById(999);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void testTotalProductos() {
        // Arrange
        when(productRepository.count()).thenReturn(25L);

        // Act
        int total = productoService.totalProductos();

        // Assert
        assertEquals(25, total);
        verify(productRepository, times(1)).count();
    }

    @Test
    void testObtenerProductosEcologicos() {
        // Arrange
        List<Producto> productosEcologicos = Arrays.asList(productoEcologico);
        when(productRepository.findByEsEcologico(true)).thenReturn(productosEcologicos);

        // Act
        List<Producto> productos = productoService.obtenerProductosEcologicos();

        // Assert
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertTrue(productos.get(0).getEsEcologico());
        
        verify(productRepository, times(1)).findByEsEcologico(true);
    }

    @Test
    void testObtenerPorCategoria() {
        // Arrange
        List<Producto> productosCategoria1 = Arrays.asList(productoEcologico);
        when(productRepository.findByCategoriaId(1)).thenReturn(productosCategoria1);

        // Act
        List<Producto> productos = productoService.obtenerPorCategoria(1);

        // Assert
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals(1, productos.get(0).getCategoriaId());
        
        verify(productRepository, times(1)).findByCategoriaId(1);
    }
}
