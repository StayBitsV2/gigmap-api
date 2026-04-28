package com.staybits.gigmapapi.communities.domain.model.aggregates;

import java.util.List;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "communities")
public class Community extends AuditableAbstractAggregateRoot<Community> {
    private String name;
    private String imageUrl;
    private String description;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "community_members", joinColumns = @JoinColumn(name = "community_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> members;

    public Community() {
        this.posts = List.of();
        this.members = List.of();
    }

    public Community(CreateCommunityCommand command) {
        this();
        this.name = command.name();
        this.description = command.description();
        this.imageUrl = command.imageUrl();
    }
}
