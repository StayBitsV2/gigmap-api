package com.staybits.gigmapapi.relatedevents.domain.model.aggregates;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.concerts.domain.model.aggregates.Concert;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.relatedevents.domain.model.valueobjects.EventType;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "related_events")
public class RelatedEvent extends AuditableAbstractAggregateRoot<RelatedEvent> {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @NotNull
    @Size(max = 200)
    @Column(nullable = false)
    private String titulo;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime datehour;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType tipo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConcertStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private User organizador;

    @ManyToMany
    @JoinTable(
        name = "related_event_participants",
        joinColumns = @JoinColumn(name = "related_event_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participantes = new HashSet<>();

    public RelatedEvent() {
        super();
    }

    public RelatedEvent(Concert concert, String titulo, LocalDateTime datehour, String descripcion, 
                       EventType tipo, Venue venue, ConcertStatus status, User organizador) {
        this.concert = concert;
        this.titulo = titulo;
        this.datehour = datehour;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.venue = venue;
        this.status = status;
        this.organizador = organizador;
        this.participantes = new HashSet<>();
    }
}
