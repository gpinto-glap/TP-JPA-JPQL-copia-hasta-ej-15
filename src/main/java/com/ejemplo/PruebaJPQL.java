package com.ejemplo;

import com.ejemplo.modelo.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public class PruebaJPQL {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("\n========== EJECUTANDO CONSULTAS NIVEL 1 ==========\n");

            // 1. Consulta de Entidades Completas
            System.out.println("--- Ejercicio 1: Lista completa de Facturas ---");
            List<FacturaVenta> facturas = em.createQuery("SELECT f FROM FacturaVenta f", FacturaVenta.class)
                                            .getResultList();
            for (FacturaVenta f : facturas) {
                System.out.println("Factura N°: " + f.getNumero() + " | Estado: " + f.getEstado() + " | Total: $" + f.getImporteTotal());
            }

            // 2. Proyección de Atributos Específicos
            System.out.println("\n--- Ejercicio 2: Número, Fecha e Importe Total ---");
            List<Object[]> datosProyectados = em.createQuery("SELECT f.numero, f.fechaEmision, f.importeTotal FROM FacturaVenta f", Object[].class)
                                                .getResultList();
            for (Object[] fila : datosProyectados) {
                System.out.println("N°: " + fila[0] + " | Fecha: " + fila[1] + " | Total: $" + fila[2]);
            }

            // 3. Filtrado por Igualdad
            System.out.println("\n--- Ejercicio 3: Artículos por Rubro ---");
            List<Articulo> articulos = em.createQuery("SELECT a FROM Articulo a WHERE a.rubro.denominacion = :nombreRubro", Articulo.class)
                                         .setParameter("nombreRubro", "Electrónica")
                                         .getResultList();
            for (Articulo a : articulos) {
                System.out.println("Código: " + a.getCodigo() + " | Denominación: " + a.getDenominacion());
            }

            // 4. Filtrado por Rango de Fechas
            System.out.println("\n--- Ejercicio 4: Facturas por Rango de Fechas ---");
            Calendar cal = Calendar.getInstance();
            cal.set(2026, Calendar.SEPTEMBER, 1);
            Date fechaInicio = cal.getTime();
            cal.set(2026, Calendar.SEPTEMBER, 30);
            Date fechaFin = cal.getTime();

            List<FacturaVenta> facturasRango = em.createQuery("SELECT f FROM FacturaVenta f WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin", FacturaVenta.class)
                                                 .setParameter("fechaInicio", fechaInicio)
                                                 .setParameter("fechaFin", fechaFin)
                                                 .getResultList();
            for (FacturaVenta f : facturasRango) {
                System.out.println("Factura N°: " + f.getNumero() + " | Fecha: " + f.getFechaEmision());
            }


            System.out.println("\n========== EJECUTANDO CONSULTAS NIVEL 2 ==========\n");

            // 5. Condicionales Complejos y Verificación de Nulos
            System.out.println("--- Ejercicio 5: Facturas emitidas, > $10.000 y no anuladas ---");
            List<FacturaVenta> facturasFiltro = em.createQuery(
                "SELECT f FROM FacturaVenta f WHERE f.estado = :estado AND f.importeTotal > :minImporte AND f.fechaAnulacion IS NULL", FacturaVenta.class)
                .setParameter("estado", "EMITIDA")
                .setParameter("minImporte", 10000.0)
                .getResultList();
            System.out.println("Cantidad encontradas: " + facturasFiltro.size());

            // 6. Búsqueda por Patrón de Texto (Ajustado a Cliente: denominacion y cuitCuil)
            System.out.println("\n--- Ejercicio 6: Búsqueda de Clientes por texto o CUIT ---");
            List<Cliente> clientes = em.createQuery(
                "SELECT c FROM Cliente c WHERE LOWER(c.denominacion) LIKE LOWER(:texto) OR c.cuitCuil LIKE :cuitPrefijo", Cliente.class)
                .setParameter("texto", "%juan%")
                .setParameter("cuitPrefijo", "20-%")
                .getResultList();
            for (Cliente c : clientes) {
                System.out.println("Cliente: " + c.getDenominacion() + " | CUIT/CUIL: " + c.getCuitCuil());
            }

            // 7. Valores Distintos y Ordenamiento
            System.out.println("\n--- Ejercicio 7: Estados de Factura sin duplicados (ORDER BY) ---");
            List<String> estados = em.createQuery(
                "SELECT DISTINCT f.estado FROM FacturaVenta f ORDER BY f.estado ASC", String.class)
                .getResultList();
            for (String est : estados) {
                System.out.println("Estado registrado: " + est);
            }

            // 8. Funciones de Agregación Simples
            System.out.println("\n--- Ejercicio 8: Agregaciones (COUNT, SUM, AVG) ---");
            Object[] resAgregado = em.createQuery(
                "SELECT COUNT(f), SUM(f.importeTotal), AVG(f.importeTotal) FROM FacturaVenta f", Object[].class)
                .getSingleResult();
            System.out.println("Total Facturas: " + resAgregado[0]);
            System.out.println("Monto Acumulado: $" + resAgregado[1]);
            System.out.println("Monto Promedio: $" + resAgregado[2]);

            // 9. Operador de Inclusión (IN)
            System.out.println("\n--- Ejercicio 9: Puntos de Venta (IN) ---");
            List<Integer> listaNumeros = Arrays.asList(1, 2, 5);
            List<PuntoVenta> puntosVenta = em.createQuery(
                "SELECT pv FROM PuntoVenta pv WHERE pv.numero IN :listaNumeros", PuntoVenta.class)
                .setParameter("listaNumeros", listaNumeros)
                .getResultList();
            for (PuntoVenta pv : puntosVenta) {
                System.out.println("Punto de Venta N°: " + pv.getNumero() + " | Desc: " + pv.getDescripcion());
            }


            System.out.println("\n========== EJECUTANDO CONSULTAS NIVEL 3 ==========\n");

            // 10. Navegación Implícita por Relaciones
            System.out.println("--- Ejercicio 10: Facturas por usuario de carga ---");
            List<FacturaVenta> facturasUsuario = em.createQuery(
                "SELECT f FROM FacturaVenta f WHERE f.usuarioCarga.usuario = :username", FacturaVenta.class)
                .setParameter("username", "admin")
                .getResultList();
            for (FacturaVenta f : facturasUsuario) {
                System.out.println("Factura N°: " + f.getNumero() + " | Creada por: " + f.getUsuarioCarga().getUsuario());
            }

            // 11. Cláusula INNER JOIN Explícita
            System.out.println("\n--- Ejercicio 11: Detalles por Punto de Venta ---");
            List<FacturaVentaDetalle> detallesPV = em.createQuery(
                "SELECT d FROM FacturaVentaDetalle d INNER JOIN d.factura f WHERE f.puntoVenta.numero = :numPV", FacturaVentaDetalle.class)
                .setParameter("numPV", 1)
                .getResultList();
            for (FacturaVentaDetalle d : detallesPV) {
                System.out.println("Detalle: " + d.getDescripcion() + " | Cantidad: " + d.getCantidad());
            }

            // 12. Cláusula LEFT JOIN (Inclusión de Nulos)
            System.out.println("\n--- Ejercicio 12: Artículos y sus marcas (LEFT JOIN) ---");
            List<Object[]> articulosMarcas = em.createQuery(
                "SELECT a.denominacion, m.denominacion FROM Articulo a LEFT JOIN a.marca m", Object[].class)
                .getResultList();
            for (Object[] fila : articulosMarcas) {
                System.out.println("Artículo: " + fila[0] + " | Marca: " + (fila[1] != null ? fila[1] : "SIN MARCA"));
            }

            // 13. Navegación Multinivel con JOINs Combinados
            System.out.println("\n--- Ejercicio 13: Facturas con detalle de marca específica ---");
            List<FacturaVenta> facturasMarca = em.createQuery(
                "SELECT DISTINCT f FROM FacturaVenta f JOIN f.detalles d JOIN d.listaPrecioArticulo lpa JOIN lpa.articulo a WHERE a.marca.denominacion = :nombreMarca", FacturaVenta.class)
                .setParameter("nombreMarca", "Logitech")
                .getResultList();
            System.out.println("Facturas encontradas para esa marca: " + facturasMarca.size());

            // 14. Subconsulta en Cláusula WHERE
            System.out.println("\n--- Ejercicio 14: Facturas con importe mayor al promedio ---");
            List<FacturaVenta> facturasSuperiores = em.createQuery(
                "SELECT f FROM FacturaVenta f WHERE f.importeTotal > (SELECT AVG(f2.importeTotal) FROM FacturaVenta f2)", FacturaVenta.class)
                .getResultList();
            for (FacturaVenta f : facturasSuperiores) {
                System.out.println("Factura N°: " + f.getNumero() + " | Importe: $" + f.getImporteTotal());
            }

            // 15. Filtrado por Cliente (Ajustado a cuitCuil)
            System.out.println("\n--- Ejercicio 15: Facturas asociadas a CUIT de Cliente ---");
            List<FacturaVenta> facturasCliente = em.createQuery(
                "SELECT f FROM FacturaVenta f WHERE f.cliente.cuitCuil = :cuitCliente", FacturaVenta.class)
                .setParameter("cuitCliente", "20-30456789-8")
                .getResultList();
            for (FacturaVenta f : facturasCliente) {
                System.out.println("Factura N°: " + f.getNumero() + " | CUIT/CUIL Cliente: " + f.getCliente().getCuitCuil());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}