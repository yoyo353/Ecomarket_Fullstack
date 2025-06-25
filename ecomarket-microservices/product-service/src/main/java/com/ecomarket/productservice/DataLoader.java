package com.ecomarket.productservice;

import com.ecomarket.productservice.model.Producto;
import com.ecomarket.productservice.repository.ProductRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        
        // Estados posibles
        String[] estados = {"ACTIVE", "INACTIVE", "DISCONTINUED"};
        
        System.out.println("🌱 Generando datos falsos para EcoMarket...");
        
        // Generar 50 productos ecológicos
        for (int i = 0; i < 50; i++) {
            Producto producto = new Producto();
            
            // Generar SKU único
            producto.setCodigoSKU("ECO-" + String.format("%03d", i + 1));
            
            // Nombres de productos ecológicos creativos
            String nombreProducto = generarNombreProductoEcologico(faker, random);
            producto.setNombreProducto(nombreProducto);
            
            // Precios realistas
            double precioCompra = faker.number().randomDouble(2, 1, 50);
            // Generar margen de ganancia entre 0.3 y 0.8 (30% a 80%)
            double margenGanancia = 0.3 + (0.5 * random.nextDouble());
            double precioUnitario = precioCompra * (1 + margenGanancia);
            
            producto.setPrecioCompra(precioCompra);
            producto.setMargenGanancia(margenGanancia);
            producto.setPrecioUnitario(Math.round(precioUnitario * 100.0) / 100.0);
            
            // Descripción ecológica
            producto.setDescripcion(generarDescripcionEcologica(faker));
            
            // Categoría (1-5)
            producto.setCategoriaId(random.nextInt(5) + 1);
            
            // Proveedor (1-10)
            producto.setProveedorPrincipalId(random.nextInt(10) + 1);
            
            // 80% de productos son ecológicos
            producto.setEsEcologico(random.nextDouble() < 0.8);
            
            // Fecha de registro (evitar método deprecated)
            long diasAtras = (long) (random.nextDouble() * 365);
            LocalDateTime fecha = LocalDateTime.now().minusDays(diasAtras);
            producto.setFechaRegistro(fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // Estado (90% activos)
            producto.setEstado(random.nextDouble() < 0.9 ? "ACTIVE" : estados[random.nextInt(estados.length)]);
            
            productRepository.save(producto);
        }
        
        System.out.println("✅ Se han generado " + productRepository.count() + " productos en la base de datos H2");
        System.out.println("🔗 Accede a la consola H2 en: http://localhost:8090/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:ecomarketdb");
        System.out.println("   Usuario: sa");
        System.out.println("   Contraseña: (vacía)");
    }
    
    private String generarNombreProductoEcologico(Faker faker, Random random) {
        String[] tiposProducto = {
            "Jabón de", "Champú de", "Aceite de", "Crema de", "Bálsamo de",
            "Pasta de dientes de", "Desodorante de", "Mascarilla de", "Serum de",
            "Bolsa de", "Cepillo de", "Pajitas de", "Vaso de", "Plato de",
            "Detergente de", "Limpiador de", "Cera de", "Spray de"
        };
        
        String[] ingredientes = {
            "coco", "lavanda", "tea tree", "aloe vera", "romero", "menta", "eucalipto",
            "bambú", "hemp", "algodón orgánico", "caña de azúcar", "maíz", "avena",
            "karité", "jojoba", "argán", "rosa mosqueta", "calendula", "manzanilla"
        };
        
        return tiposProducto[random.nextInt(tiposProducto.length)] + " " + 
               ingredientes[random.nextInt(ingredientes.length)];
    }
    
    private String generarDescripcionEcologica(Faker faker) {
        String[] caracteristicas = {
            "100% natural y biodegradable",
            "Libre de químicos nocivos",
            "Certificado orgánico",
            "Empaque sostenible",
            "Cruelty-free",
            "Vegano",
            "Sin parabenos ni sulfatos",
            "Comercio justo",
            "Zero waste"
        };
        
        return "Producto ecológico " + caracteristicas[new Random().nextInt(caracteristicas.length)] + 
               ". " + faker.lorem().sentence(8) + " Ideal para el cuidado responsable del medio ambiente.";
    }
}
