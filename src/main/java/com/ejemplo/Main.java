package com.ejemplo;

import com.ejemplo.modelo.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Usuario auditor
            Usuario admin = new Usuario();
            admin.setUsuario("admin");
            admin.setClave("1234");
            admin.setNombre("Juan");
            admin.setApellido("Pérez");
            em.persist(admin);

            // 2. Punto de Venta
            PuntoVenta pv = new PuntoVenta();
            pv.setNumero(1);
            pv.setDescripcion("Sucursal Central");
            pv.setFechaAlta(new Date());
            pv.setFechaModificacion(new Date());
            pv.setUsuarioCarga(admin);
            pv.setUsuarioModificacion(admin);
            em.persist(pv);

            // 3. Artículo y Lista de Precio
            Articulo articulo = new Articulo();
            articulo.setCodigo("A001");
            articulo.setDenominacion("Notebook Gamer");
            articulo.setFechaAlta(new Date());
            articulo.setFechaModificacion(new Date());
            articulo.setUsuarioCarga(admin);
            articulo.setUsuarioModificacion(admin);
            em.persist(articulo);

            ListaPrecio lp = new ListaPrecio();
            lp.setCodigo("LP1");
            lp.setDenominacion("Minorista");
            lp.setFechaAlta(new Date());
            lp.setFechaModificacion(new Date());
            lp.setUsuarioCarga(admin);
            lp.setUsuarioModificacion(admin);
            em.persist(lp);

            ListaPrecioArticulo lpa = new ListaPrecioArticulo();
            lpa.setArticulo(articulo);
            lpa.setListaPrecio(lp);
            lpa.setPrecioVenta(1500.0);
            lpa.setFechaAlta(new Date());
            lpa.setFechaModificacion(new Date());
            lpa.setUsuarioCarga(admin);
            lpa.setUsuarioModificacion(admin);
            em.persist(lpa);

            // 4. TipoMoneda (con auditoría completa)
            TipoMoneda tipoMoneda = new TipoMoneda();
            tipoMoneda.setCodigoAfip("123ABC");
            tipoMoneda.setDenominacion("Pesos");
            tipoMoneda.setSimbolo("ars");
            tipoMoneda.setFechaAlta(new Date());
            tipoMoneda.setFechaModificacion(new Date());
            tipoMoneda.setUsuarioCarga(admin);
            tipoMoneda.setUsuarioModificacion(admin);
            em.persist(tipoMoneda);

            // 5. CondicionIva (con auditoría completa)
            CondicionIva condicionIva = new CondicionIva();
            condicionIva.setCodigoAfip(1234);
            condicionIva.setDenominacion("inscripto");
            condicionIva.setFechaAlta(new Date());
            condicionIva.setFechaModificacion(new Date());
            condicionIva.setUsuarioCarga(admin);
            condicionIva.setUsuarioModificacion(admin);
            em.persist(condicionIva);

            // 6. Cabecera de Factura
            FacturaVenta factura = new FacturaVenta();
            factura.setNumero(1001L);
            factura.setFechaEmision(new Date());
            factura.setPuntoVenta(pv);
            factura.setEstado("EMITIDA");
            factura.setFechaAlta(new Date());
            factura.setFechaModificacion(new Date());
            factura.setUsuarioCarga(admin);
            factura.setUsuarioModificacion(admin);
            factura.setCliente(null);
            factura.setTipoMoneda(tipoMoneda);
            factura.setCondicionIva(condicionIva);

            // 7. Detalles
            FacturaVentaDetalle d1 = new FacturaVentaDetalle();
            d1.setListaPrecioArticulo(lpa);
            d1.setDescripcion("Notebook Gamer 16GB");
            d1.setCantidad(2);
            d1.setPrecioUnitario(1500.0);
            d1.setImporteSubtotal(3000.0);

            factura.addDetalle(d1);
            factura.setImporteTotal(3000.0);

            // 8. Persistencia de Factura
            em.persist(factura);

            em.getTransaction().commit();
            System.out.println("¡Factura guardada con éxito en la Base de Datos!");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}