package com.staybits.gigmapapi.concerts.domain.model.entities;

import com.staybits.gigmapapi.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "venues")
public class Venue extends AuditableModel {
    
    @NotNull
    @Column(nullable = false, length = 200)
    private String name;
    
    @NotNull
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitud;
    
    @NotNull
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitud;
    
    @NotNull
    @Column(nullable = false, length = 500)
    private String direccion;
    
    @NotNull
    @Column(nullable = false)
    private Integer capacidad;
    
    public Venue() {
    }
    
    public Venue(String name, BigDecimal latitud, BigDecimal longitud, String direccion, Integer capacidad) {
        this.name = name;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.capacidad = capacidad;
    }
}
