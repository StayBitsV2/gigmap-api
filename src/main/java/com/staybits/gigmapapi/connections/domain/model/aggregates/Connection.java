package com.staybits.gigmapapi.connections.domain.model.aggregates;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "connections")
public class Connection extends AuditableAbstractAggregateRoot<Connection> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan1_id", nullable = false)
    private User fan1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan2_id", nullable = false)
    private User fan2;

    public Connection() {
        super();
    }

    public Connection(User fan1, User fan2) {
        this();
        if (fan1.getId() < fan2.getId()) {
            this.fan1 = fan1;
            this.fan2 = fan2;
        } else {
            this.fan1 = fan2;
            this.fan2 = fan1;
        }
    }

    public static Connection createFrom(ConnectionRequest request) {
        if (request.getStatus() != com.staybits.gigmapapi.connections.domain.model.valueobjects.ConnectionRequestStatus.ACCEPTED) {
            throw new IllegalArgumentException("Cannot create connection from a non-ACCEPTED request");
        }
        return new Connection(request.getRequester(), request.getTarget());
    }

    public User getOtherUser(User oneOfThem) {
        if (fan1.getId().equals(oneOfThem.getId())) {
            return fan2;
        } else if (fan2.getId().equals(oneOfThem.getId())) {
            return fan1;
        }
        throw new IllegalArgumentException("User is not part of this connection");
    }
}
