package com.staybits.gigmapapi.communities.domain.model.aggregates;

import java.util.ArrayList;
import java.util.List;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.communities.domain.events.PostCreatedEvent;
import com.staybits.gigmapapi.communities.domain.events.PostLikedEvent;
import com.staybits.gigmapapi.communities.domain.model.commands.CreatePostCommand;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Post extends AuditableAbstractAggregateRoot<Post> {
    private String content;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "likes")
    private List<User> likedBy = new ArrayList<User>();

    public Post() {}

    public void registerLikedEvent(String likerUsername) {
        this.registerEvent(new PostLikedEvent(this.getId(), likerUsername, this.user.getId()));
    }

    public Post(CreatePostCommand command, Community community, User user) {
        this.content = command.content();
        this.imageUrl = command.imageUrl();
        this.community = community;
        this.user = user;

        registerEvent(new PostCreatedEvent(this.getId(), this.content, this.community.getName(), this.user.getUsername(), this.user.getId()));
    }
}
