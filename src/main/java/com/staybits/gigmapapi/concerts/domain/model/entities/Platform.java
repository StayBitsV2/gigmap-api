package com.staybits.gigmapapi.concerts.domain.model.entities;

import com.staybits.gigmapapi.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "platforms")
public class Platform extends AuditableModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;
    
    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    public Platform() {
    }
    
    public Platform(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
