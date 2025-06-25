package com.ecomarket.productservice.controller;

import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@ActiveProfiles("test")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoEcologico;
    private List<Producto> listaProductos;

    @BeforeEach
    void setUp() {
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

        listaProductos = Arrays.asList(productoEcologico);
    }

    @Test
    void testObtenerTodos() throws Exception {
        // Arrange
        when(productoService.obtenerTodos()).thenReturn(listaProductos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreProducto", is("Jabón de lavanda ecológico")))
                .andExpect(jsonPath("$[0].codigoSKU", is("ECO-001")))
                .andExpect(jsonPath("$[0].esEcologico", is(true)));

        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerPorId_ProductoExistente() throws Exception {
        // Arrange
        when(productoService.buscarPorId(1)).thenReturn(productoEcologico);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productoId", is(1)))
                .andExpect(jsonPath("$.nombreProducto", is("Jabón de lavanda ecológico")))
                .andExpect(jsonPath("$.codigoSKU", is("ECO-001")));

        verify(productoService, times(1)).buscarPorId(1);
    }

    @Test
    void testObtenerPorId_ProductoNoExistente() throws Exception {
        // Arrange
        when(productoService.buscarPorId(999)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/999"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).buscarPorId(999);
    }

    @Test
    void testObtenerPorSKU_ProductoExistente() throws Exception {
        // Arrange
        when(productoService.buscarPorSKU("ECO-001")).thenReturn(productoEcologico);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/sku/ECO-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.codigoSKU", is("ECO-001")))
                .andExpect(jsonPath("$.nombreProducto", is("Jabón de lavanda ecológico")));

        verify(productoService, times(1)).buscarPorSKU("ECO-001");
    }

    @Test
    void testCrearProducto() throws Exception {
        // Arrange
        when(productoService.guardar(any(Producto.class))).thenReturn(productoEcologico);

        // Act & Assert
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoEcologico)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreProducto", is("Jabón de lavanda ecológico")))
                .andExpect(jsonPath("$.codigoSKU", is("ECO-001")));

        verify(productoService, times(1)).guardar(any(Producto.class));
    }

    @Test
    void testActualizarProducto_ProductoExistente() throws Exception {
        // Arrange
        when(productoService.actualizar(eq(1), any(Producto.class))).thenReturn(productoEcologico);

        // Act & Assert
        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoEcologico)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombreProducto", is("Jabón de lavanda ecológico")));

        verify(productoService, times(1)).actualizar(eq(1), any(Producto.class));
    }

    @Test
    void testActualizarProducto_ProductoNoExistente() throws Exception {
        // Arrange
        when(productoService.actualizar(eq(999), any(Producto.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/v1/productos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoEcologico)))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).actualizar(eq(999), any(Producto.class));
    }

    @Test
    void testEliminarProducto_ProductoExistente() throws Exception {
        // Arrange
        when(productoService.eliminar(1)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminar(1);
    }

    @Test
    void testEliminarProducto_ProductoNoExistente() throws Exception {
        // Arrange
        when(productoService.eliminar(999)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/productos/999"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).eliminar(999);
    }

    @Test
    void testTotalProductos() throws Exception {
        // Arrange
        when(productoService.totalProductos()).thenReturn(25);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/total"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("25"));

        verify(productoService, times(1)).totalProductos();
    }

    @Test
    void testObtenerProductosEcologicos() throws Exception {
        // Arrange
        when(productoService.obtenerProductosEcologicos()).thenReturn(listaProductos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/ecologicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].esEcologico", is(true)));

        verify(productoService, times(1)).obtenerProductosEcologicos();
    }

    @Test
    void testObtenerPorCategoria() throws Exception {
        // Arrange
        when(productoService.obtenerPorCategoria(1)).thenReturn(listaProductos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoriaId", is(1)));

        verify(productoService, times(1)).obtenerPorCategoria(1);
    }
}
