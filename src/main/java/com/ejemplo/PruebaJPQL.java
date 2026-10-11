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
            cal.add(Calendar.DAY_OF_MONTH, -7);
            Date fechaInicio = cal.getTime(); // Hace 7 días
            Date fechaFin = new Date();       // Hoy

            List<FacturaVenta> facturasRango = em.createQuery("SELECT f FROM FacturaVenta f WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin", FacturaVenta.class)
                                                 .setParameter("fechaInicio", fechaInicio)
                                                 .setParameter("fechaFin", fechaFin)
                                                 .getResultList();
            for (FacturaVenta f : facturasRango) {
                System.out.println("Factura N°: " + f.getNumero() + " | Fecha: " + f.getFechaEmision());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}