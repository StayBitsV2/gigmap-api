package com.staybits.gigmapapi.concerts.domain.model.aggregates;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.domain.model.valueobjects.Role;
import com.staybits.gigmapapi.concerts.domain.model.entities.Platform;
import com.staybits.gigmapapi.concerts.domain.model.entities.Venue;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.ConcertStatus;
import com.staybits.gigmapapi.concerts.domain.model.valueobjects.Genre;
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
@Table(name = "concerts")
public class Concert extends AuditableAbstractAggregateRoot<Concert> {

    @NotNull
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime datehour;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;

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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @ManyToMany
    @JoinTable(
        name = "concert_attendees",
        joinColumns = @JoinColumn(name = "concert_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> attendees = new HashSet<>();

    public Concert() {
        super();
    }

    public Concert(String title, LocalDateTime datehour, String description, String imageUrl, Venue venue, ConcertStatus status, User user, Genre genre, Platform platform) {
        this.title = title;
        this.datehour = datehour;
        this.description = description;
        this.imageUrl = imageUrl;
        this.venue = venue;
        this.status = status;
        this.user = user;
        this.genre = genre;
        this.platform = platform;
        this.attendees = new HashSet<>();
    }

    /**
     * Business logic to validate that the user has ARTIST role
     */
    public boolean isValidArtist() {
        return this.user != null && this.user.getRole() == Role.ARTIST;
    }
}
