package com.staybits.gigmapapi.communities.domain.model.aggregates;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "threads")
public class Thread extends AuditableAbstractAggregateRoot<Thread> {
    private String title;

    private String content;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Thread() {}

    public Thread(String title, String content, String imageUrl, Community community, User user) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.community = community;
        this.user = user;
    }
}
