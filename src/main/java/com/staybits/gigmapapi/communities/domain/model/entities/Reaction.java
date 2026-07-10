package com.staybits.gigmapapi.communities.domain.model.entities;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.communities.domain.model.valueobjects.ReactionEmoji;
import com.staybits.gigmapapi.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reactions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "thread_id", "emoji"}),
    @UniqueConstraint(columnNames = {"user_id", "comment_id", "emoji"})
})
public class Reaction extends AuditableModel {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionEmoji emoji;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "thread_id")
    private Long threadId;

    @Column(name = "comment_id")
    private Long commentId;

    public Reaction() {}

    public Reaction(ReactionEmoji emoji, User user, Long threadId, Long commentId) {
        this.emoji = emoji;
        this.user = user;
        this.threadId = threadId;
        this.commentId = commentId;
    }
}
