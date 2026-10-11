package com.ejemplo.modelo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass // Le dice a JPA que no sea tabla, sino base para sus clases hijas
public abstract class EntityId {

    @Id // Marca la clave primaria (PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID Autoincremental
    protected Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}