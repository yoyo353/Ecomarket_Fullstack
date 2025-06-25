package com.ecomarket.orderservice;


import com.ecomarket.orderservice.model.Pedido;
import com.ecomarket.orderservice.model.DetallePedido;
import com.ecomarket.orderservice.repository.PedidoRepository;
import com.ecomarket.orderservice.repository.DetallePedidoRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * DataLoader para generar datos de prueba automáticamente
 * Se ejecuta solo en perfil 'dev' al iniciar la aplicación
 */
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    private final Faker faker = new Faker();
    private final Random random = new Random();
    
    // Arrays para datos realistas
    private final String[] ESTADOS = {"PENDIENTE", "EN_PROCESO", "COMPLETADO", "CANCELADO", "ENVIADO"};
    private final String[] CIUDADES = {"Santiago", "Valparaíso", "Concepción", "La Serena", "Antofagasta", 
                                      "Temuco", "Rancagua", "Talca", "Arica", "Iquique"};
    private final String[] DIRECCIONES_BASE = {"Av. Providencia", "Calle Las Flores", "Pasaje Los Pinos", 
                                              "Los Robles", "Av. Las Condes", "Calle San Martín", 
                                              "Av. O'Higgins", "Pedro de Valdivia"};

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🌱 Iniciando carga de datos falsos para EcoMarket Order Service...");
        
        // Verificar si ya existen datos
        if (pedidoRepository.count() > 0) {
            System.out.println("⚠️  Ya existen datos en la base de datos. Saltando carga inicial.");
            return;
        }
        
        // Generar pedidos con sus detalles
        generarPedidosConDetalles();
        
        System.out.println("✅ Carga de datos completada exitosamente!");
        mostrarEstadisticas();
    }
    
    private void generarPedidosConDetalles() {
        System.out.println("📦 Generando pedidos y detalles...");
        
        // Generar 50 pedidos con detalles
        for (int i = 0; i < 50; i++) {
            // Crear pedido
            Pedido pedido = crearPedidoFalso();
            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            
            // Crear detalles para el pedido (entre 1 y 5 productos)
            int cantidadDetalles = random.nextInt(5) + 1;
            double subtotalPedido = 0.0;
            
            for (int j = 0; j < cantidadDetalles; j++) {
                DetallePedido detalle = crearDetallePedidoFalso(pedidoGuardado.getPedidoId());
                detallePedidoRepository.save(detalle);
                subtotalPedido += detalle.getSubTotal();
            }
            
            // Actualizar subtotal y total del pedido
            actualizarTotalesPedido(pedidoGuardado, subtotalPedido);
        }
    }
    
    private Pedido crearPedidoFalso() {
        Pedido pedido = new Pedido();
        
        // Fecha de pedido (últimos 30 días)
        LocalDate fechaPedido = LocalDate.now().minusDays(random.nextInt(30));
        pedido.setFechaDePedido(fechaPedido.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        // Cliente ID (simulamos 20 clientes diferentes)
        pedido.setClienteId(random.nextInt(20) + 1);
        
        // Estado aleatorio con distribución realista
        pedido.setEstado(obtenerEstadoRealista());
        
        // Descuento (0%, 5%, 10%, 15% o 20%)
        double[] descuentosPosibles = {0.0, 5.0, 10.0, 15.0, 20.0};
        pedido.setDescuento(descuentosPosibles[random.nextInt(descuentosPosibles.length)]);
        
        // Método de pago (1-5: Efectivo, Tarjeta, Transferencia, PayPal, etc.)
        pedido.setMetodoPagoId(random.nextInt(5) + 1);
        
        // Usuario ID (simulamos 10 usuarios/vendedores)
        pedido.setUsuarioId(random.nextInt(10) + 1);
        
        // Dirección de envío realista
        String direccion = DIRECCIONES_BASE[random.nextInt(DIRECCIONES_BASE.length)] + " " 
                          + faker.number().numberBetween(100, 9999);
        pedido.setDireccionEnvio(direccion);
        
        // Ciudad de envío
        pedido.setCiudadEnvio(CIUDADES[random.nextInt(CIUDADES.length)]);
        
        // Notas opcionales (50% de probabilidad)
        if (random.nextBoolean()) {
            pedido.setNotas(generarNotaRealista());
        }
        
        return pedido;
    }
    
    private DetallePedido crearDetallePedidoFalso(Integer pedidoId) {
        DetallePedido detalle = new DetallePedido();
        
        detalle.setPedidoId(pedidoId);
        
        // Producto ID (simulamos 100 productos disponibles)
        detalle.setProductoId(random.nextInt(100) + 1);
        
        // Precio unitario realista para productos eco-friendly ($2.000 - $25.000)
        double precioUnitario = faker.number().randomDouble(2, 2000, 25000);
        detalle.setPrecioUnitario(precioUnitario);
        
        // Cantidad (1-10 unidades)
        int cantidad = random.nextInt(10) + 1;
        detalle.setCantidad(cantidad);
        
        // Calcular subtotal
        double subTotal = precioUnitario * cantidad;
        detalle.setSubTotal(subTotal);
        
        return detalle;
    }
    
    private void actualizarTotalesPedido(Pedido pedido, double subtotal) {
        pedido.setSubtotal(subtotal);
        
        // Aplicar descuento
        double descuento = pedido.getDescuento() != null ? pedido.getDescuento() : 0.0;
        double total = subtotal - (subtotal * descuento / 100);
        pedido.setTotal(total);
        
        pedidoRepository.save(pedido);
    }
    
    private String obtenerEstadoRealista() {
        // Distribución realista de estados
        int probabilidad = random.nextInt(100);
        if (probabilidad < 40) return "COMPLETADO";      // 40%
        if (probabilidad < 65) return "PENDIENTE";       // 25%
        if (probabilidad < 85) return "EN_PROCESO";      // 20%
        if (probabilidad < 95) return "ENVIADO";         // 10%
        return "CANCELADO";                              // 5%
    }
    
    private String generarNotaRealista() {
        String[] notas = {
            "Entrega en horario de oficina",
            "Llamar antes de entregar",
            "Dejar en conserjería",
            "Cliente VIP - manejo especial",
            "Producto frágil - cuidado extra",
            "Entrega urgente",
            "Sin contacto - dejar en puerta",
            "Cliente frecuente",
            "Verificar dirección antes de entrega",
            "Pedido especial - revisar contenido"
        };
        return notas[random.nextInt(notas.length)];
    }
    
    private void mostrarEstadisticas() {
        long totalPedidos = pedidoRepository.count();
        long totalDetalles = detallePedidoRepository.count();
        
        System.out.println("\n📊 ESTADÍSTICAS DE DATOS GENERADOS:");
        System.out.println("================================================");
        System.out.println("📦 Total de Pedidos: " + totalPedidos);
        System.out.println("📋 Total de Detalles: " + totalDetalles);
        System.out.println("🎯 Promedio de productos por pedido: " + 
                          String.format("%.1f", (double) totalDetalles / totalPedidos));
        
        // Estadísticas por estado
        System.out.println("\n📈 DISTRIBUCIÓN POR ESTADOS:");
        for (String estado : ESTADOS) {
            long count = pedidoRepository.countByEstado(estado);
            if (count > 0) {
                System.out.println("   " + estado + ": " + count + " pedidos");
            }
        }
        
        // Estadísticas por ciudad
        System.out.println("\n🌎 DISTRIBUCIÓN POR CIUDADES:");
        for (String ciudad : CIUDADES) {
            long count = pedidoRepository.findByCiudadEnvio(ciudad).size();
            if (count > 0) {
                System.out.println("   " + ciudad + ": " + count + " pedidos");
            }
        }
        
        System.out.println("================================================\n");
    }
}
