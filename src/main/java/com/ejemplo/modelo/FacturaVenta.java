package com.ejemplo.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "factura_venta")
public class FacturaVenta extends AuditoriaApp {

    private Long numero;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;

    @ManyToOne
    @JoinColumn(name = "punto_venta_id", nullable = false)
    private PuntoVenta puntoVenta;
       // Cliente puede ser opcional (por ejemplo, en ventas a consumidor final sin identificar)
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    private Cliente cliente;

    // Condición de IVA es obligatoria para la facturación
    @ManyToOne
    @JoinColumn(name = "condicion_iva_id", nullable = false)
    private CondicionIva condicionIva;

    // Tipo de Moneda es obligatorio para registrar el importe
    @ManyToOne
    @JoinColumn(name = "tipo_moneda_id", nullable = false)
    private TipoMoneda tipoMoneda;

    private double importeCobrado;
    private double importeSaldo;

    @Column(nullable = false)
    private double importeTotal;

    private String cae;

    @Temporal(TemporalType.DATE)
    private Date caeFechaVencimiento;

    private String resultadoAfip;
    private String motivoRechazo;

    @Column(nullable = false)
    private String estado;

    @Temporal(TemporalType.DATE)
    private Date fechaAnulacion;

    private String observaciones;

    // Relación OneToMany con Cascade.ALL (Requisito Clave)
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaVentaDetalle> detalles = new ArrayList<>();

    // Helper para mantener sincronizada la relación bidireccional
    public void addDetalle(FacturaVentaDetalle detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }

    // Getters y Setters
    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }
    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }
    public PuntoVenta getPuntoVenta() { return puntoVenta; }
    public void setPuntoVenta(PuntoVenta puntoVenta) { this.puntoVenta = puntoVenta; }
    public double getImporteCobrado() { return importeCobrado; }
    public void setImporteCobrado(double importeCobrado) { this.importeCobrado = importeCobrado; }
    public double getImporteSaldo() { return importeSaldo; }
    public void setImporteSaldo(double importeSaldo) { this.importeSaldo = importeSaldo; }
    public double getImporteTotal() { return importeTotal; }
    public void setImporteTotal(double importeTotal) { this.importeTotal = importeTotal; }
    public String getCae() { return cae; }
    public void setCae(String cae) { this.cae = cae; }
    public Date getCaeFechaVencimiento() { return caeFechaVencimiento; }
    public void setCaeFechaVencimiento(Date caeFechaVencimiento) { this.caeFechaVencimiento = caeFechaVencimiento; }
    public String getResultadoAfip() { return resultadoAfip; }
    public void setResultadoAfip(String resultadoAfip) { this.resultadoAfip = resultadoAfip; }
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Date getFechaAnulacion() { return fechaAnulacion; }
    public void setFechaAnulacion(Date fechaAnulacion) { this.fechaAnulacion = fechaAnulacion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<FacturaVentaDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<FacturaVentaDetalle> detalles) { this.detalles = detalles; }
    // Getter y Setter para Cliente
public Cliente getCliente() {
    return cliente;
}

public void setCliente(Cliente cliente) {
    this.cliente = cliente;
}

// Getter y Setter para CondicionIva
public CondicionIva getCondicionIva() {
    return condicionIva;
}

public void setCondicionIva(CondicionIva condicionIva) {
    this.condicionIva = condicionIva;
}

// Getter y Setter para TipoMoneda
public TipoMoneda getTipoMoneda() {
    return tipoMoneda;
}

public void setTipoMoneda(TipoMoneda tipoMoneda) {
    this.tipoMoneda = tipoMoneda;
}
}
